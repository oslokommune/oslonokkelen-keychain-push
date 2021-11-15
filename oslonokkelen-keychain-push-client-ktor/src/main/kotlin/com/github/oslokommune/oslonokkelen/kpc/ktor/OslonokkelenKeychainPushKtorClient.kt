package com.github.oslokommune.oslonokkelen.kpc.ktor

import com.github.oslokommune.oslonokkelen.keychainpush.proto.KeychainPushApi
import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainDeleteRequest
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryInfo
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactorySummary
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest
import com.github.oslokommune.oslonokkelen.kpc.serialization.KeychainDeleteSerializer
import com.github.oslokommune.oslonokkelen.kpc.serialization.KeychainPushSerializer
import com.google.protobuf.GeneratedMessageV3
import io.ktor.client.HttpClient
import io.ktor.client.features.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
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
        header(OslonokkelenKeychainPushClient.clientIdHeaderName, config.systemId)
        header(OslonokkelenKeychainPushClient.clientApiKeyHeaderName, config.apiSecret)
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
        try {
            val uri = config.formatKeychainPushUri(keychainId).toString()
            val ktorRequest = client.post<HttpStatement>(uri) {
                body = KeychainPushSerializer.toProtobuf(request).toByteArray()
                requestBuilder(this)
            }

            val httpResponse = ktorRequest.execute()

            readSuccessfulResponsePayload(
                    factory = KeychainPushApi.PushKeychainRequest.OkResponse::parseFrom,
                    httpResponse = httpResponse,
                    expectedType = "push-ok"
            )
        } catch (ex: OslonokkelenKeychainPushClient.ClientException) {
            throw ex
        } catch (ex: Exception) {
            throw OslonokkelenKeychainPushClient.ClientException.Unknown(
                    message = "Failed to push ${keychainId.value}",
                    cause = ex
            )
        }
    }

    override suspend fun listFactories(): List<KeychainFactorySummary> {
        return try  {
            val uri = config.formatKeychainFactoryListUri().toString()
            val ktorRequest = client.post<HttpStatement>(uri) {
                body = KeychainPushApi.ListKeychainFactoriesRequest.newBuilder()
                    .build()
                    .toByteArray()

                requestBuilder(this)
            }

            val httpResponse = ktorRequest.execute()

            val response = readSuccessfulResponsePayload(
                factory = KeychainPushApi.ListKeychainFactoriesRequest.ListResponse::parseFrom,
                httpResponse = httpResponse,
                expectedType = "factories-list"
            )

            response.summaryList.map { entry ->
                KeychainFactorySummary(
                    id = KeychainFactoryId(entry.id),
                    title = entry.title
                )
            }
        } catch (ex: OslonokkelenKeychainPushClient.ClientException) {
            throw ex
        } catch (ex: Exception) {
            throw OslonokkelenKeychainPushClient.ClientException.Unknown(
                message = "Failed to list keychain factories",
                cause = ex
            )
        }
    }

    override suspend fun delete(keychainId: KeychainId, request: KeychainDeleteRequest) {
        try {
            val uri = config.formatKeychainDeleteUri(keychainId).toString()
            val ktorRequest = client.post<HttpStatement>(uri) {
                body = KeychainDeleteSerializer.toProtobuf(request).toByteArray()
                requestBuilder(this)
            }

            val httpResponse = ktorRequest.execute()

            readSuccessfulResponsePayload(
                factory = KeychainPushApi.PushKeychainRequest.OkResponse::parseFrom,
                httpResponse = httpResponse,
                expectedType = "delete-ok"
            )
        } catch (ex: OslonokkelenKeychainPushClient.ClientException) {
            throw ex
        } catch (ex: Exception) {
            throw OslonokkelenKeychainPushClient.ClientException.Unknown(
                message = "Failed to delete ${keychainId.value}",
                cause = ex
            )
        }
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