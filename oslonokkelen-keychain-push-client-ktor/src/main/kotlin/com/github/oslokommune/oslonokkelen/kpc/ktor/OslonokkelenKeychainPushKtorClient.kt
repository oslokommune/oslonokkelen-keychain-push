package com.github.oslokommune.oslonokkelen.kpc.ktor

import com.github.oslokommune.oslonokkelen.keychainpush.proto.KeychainPushApi
import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryInfo
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest
import com.google.protobuf.GeneratedMessageV3
import io.ktor.client.HttpClient
import io.ktor.client.features.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import java.time.ZoneId

/**
 * @param client Ktor http client. You are responsible for configuring this with metrics, timeouts etc
 * @param config Configuration necessary for talking with the backend api
 */
class OslonokkelenKeychainPushKtorClient(
        val client: HttpClient,
        val config: OslonokkelenKeychainPushClient.Config
) : OslonokkelenKeychainPushClient {

    private val requestBuilder: HttpRequestBuilder.() -> Unit = {
        header(OslonokkelenKeychainPushClient.clientIdHeaderName, config.clientId)
        header(OslonokkelenKeychainPushClient.clientApiKeyHeaderName, config.clientApiKey)
        accept(ContentType.Application.ProtoBuf)
        expectSuccess = false
    }


    override suspend fun pullFactoryInfo(factoryId: KeychainFactoryId): KeychainFactoryInfo {
        return try {
            val uri = config.formatKeychainFactoryInfoUri(factoryId).toString()
            val httpResponse = client.get<HttpStatement>(uri, requestBuilder).execute()
            val info = readSuccessfulResponsePayload(
                    factory = KeychainPushApi.KeychainFactoryPushInfo::parseFrom,
                    expectedType = "keychain-factory-info",
                    httpResponse = httpResponse
            )

            KeychainFactoryInfo(
                    timezone = ZoneId.of(info.timezoneId),
                    id = factoryId
            )
        } catch (ex: OslonokkelenKeychainPushClient.ClientException) {
            throw ex
        } catch (ex: Exception) {
            throw OslonokkelenKeychainPushClient.ClientException.Unknown(
                    message = "Failed to fetch information about ${factoryId.value}",
                    cause = ex
            )
        }
    }

    override suspend fun push(keychainId: KeychainId, request: KeychainPushRequest) {
        TODO("Not yet implemented")
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun <M : GeneratedMessageV3> readSuccessfulResponsePayload(httpResponse: HttpResponse, expectedType: String, factory: (ByteArray) -> M): M {
        val oslonokkelenTraceId = httpResponse.headers[OslonokkelenKeychainPushClient.traceIdHeaderName]
        val responseApplicationType = getApplicationResponseType(httpResponse, oslonokkelenTraceId)

        if (responseApplicationType == "error") {
            handleStructuredErrorResponse(httpResponse, oslonokkelenTraceId)
        }

        return if (httpResponse.status.isSuccess()) {
            if (responseApplicationType == expectedType) {
                val bytes = httpResponse.readBytes()
                factory(bytes)
            } else {
                throw OslonokkelenKeychainPushClient.ClientException.ErrorResponse(
                        traceId = oslonokkelenTraceId,
                        errorCode = KeychainPushApi.ErrorResponse.ErrorCode.UNKNOWN,
                        technicalDebugMessage = "Expected $expectedType response, but got $responseApplicationType"
                )
            }
        } else {
            throw OslonokkelenKeychainPushClient.ClientException.ErrorResponse(
                    traceId = oslonokkelenTraceId,
                    errorCode = KeychainPushApi.ErrorResponse.ErrorCode.UNKNOWN,
                    technicalDebugMessage = "Unsuccessful http response without details: ${httpResponse.status}"
            )
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun handleStructuredErrorResponse(httpResponse: HttpResponse, oslonokkelenTraceId: String?) {
        val bytes = httpResponse.readBytes()
        val error = KeychainPushApi.ErrorResponse.parseFrom(bytes)

        throw OslonokkelenKeychainPushClient.ClientException.ErrorResponse(
                traceId = oslonokkelenTraceId,
                technicalDebugMessage = error.technicalDebugMessage,
                errorCode = error.errorCode
        )
    }

    private fun getApplicationResponseType(httpResponse: HttpResponse, oslonokkelenTraceId: String?): String? {
        val responseContentType = httpResponse.contentType()
                ?: throw OslonokkelenKeychainPushClient.ClientException.ErrorResponse(
                        traceId = oslonokkelenTraceId,
                        errorCode = KeychainPushApi.ErrorResponse.ErrorCode.UNKNOWN,
                        technicalDebugMessage = "No content type in http response"
                )

        if (responseContentType.withoutParameters() != ContentType.Application.ProtoBuf) {
            throw OslonokkelenKeychainPushClient.ClientException.ErrorResponse(
                    traceId = oslonokkelenTraceId,
                    errorCode = KeychainPushApi.ErrorResponse.ErrorCode.UNKNOWN,
                    technicalDebugMessage = "Expected protobuf response, not $responseContentType"
            )
        }

        return responseContentType.parameter("type")
    }

}