package com.github.oslokommune.oslonokkelen.push.ktor

import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.push.AssetId
import com.github.oslokommune.oslonokkelen.push.OslonokkelenClientConfig
import com.github.oslokommune.oslonokkelen.push.OslonokkelenClientException
import com.github.oslokommune.oslonokkelen.push.OslonokkelenPushClient
import com.github.oslokommune.oslonokkelen.push.PushRequest
import com.github.oslokommune.oslonokkelen.push.SystemInfo
import com.github.oslokommune.oslonokkelen.push.proto.KeychainPushApiV2
import com.google.protobuf.GeneratedMessageV3
import io.ktor.client.HttpClient
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class OslonokkelenPushKtorClient(
    val client: HttpClient,
    val config: OslonokkelenClientConfig
) : OslonokkelenPushClient {

    private val requestBuilder: HttpRequestBuilder.() -> Unit = {
        header(OslonokkelenKeychainPushClient.clientIdHeaderName, config.systemId)
        header(OslonokkelenKeychainPushClient.clientApiKeyHeaderName, config.apiSecret)
        accept(ContentType.Application.ProtoBuf)
        expectSuccess = false
    }

    override suspend fun push(request: PushRequest) {
        try {
            val httpResponse = client.post(config.pushUri) {
                requestBuilder(this)
            }

            readSuccessfulResponsePayload(
                factory = KeychainPushApiV2.PushResponse::parseFrom,
                httpResponse = httpResponse,
                expectedType = "push-response"
            )
        } catch (ex: OslonokkelenClientException) {
            throw ex
        } catch (ex: Exception) {
            throw OslonokkelenClientException(
                errorCode = KeychainPushApiV2.ErrorResponse.ErrorCode.UNKNOWN,
                technicalDebugMessage = "Unexpected error while pushing permission",
                cause = ex
            )
        }
    }

    override suspend fun describeSystem(): SystemInfo {
        return try {
            val httpResponse = client.get(config.systemInfoUri) {
                requestBuilder(this)
            }

            val protobufMessage = readSuccessfulResponsePayload(
                factory = KeychainPushApiV2.SystemDescriptionResponse::parseFrom,
                httpResponse = httpResponse,
                expectedType = "system-description"
            )

            SystemInfo(
                name = protobufMessage.name,
                information = protobufMessage.information,
                assetIds = protobufMessage.assetsList.map { asset ->
                    AssetId(asset.id)
                },
                id = protobufMessage.id
            )
        } catch (ex: OslonokkelenClientException) {
            throw ex
        } catch (ex: Exception) {
            throw OslonokkelenClientException(
                errorCode = KeychainPushApiV2.ErrorResponse.ErrorCode.UNKNOWN,
                technicalDebugMessage = "Unexpected exception while looking up system information",
                cause = ex
            )
        }
    }

    private suspend fun <M : GeneratedMessageV3> readSuccessfulResponsePayload(
        httpResponse: HttpResponse,
        expectedType: String,
        factory: (ByteArray) -> M
    ): M {
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
                throw OslonokkelenClientException(
                    traceId = oslonokkelenTraceId,
                    errorCode = KeychainPushApiV2.ErrorResponse.ErrorCode.UNKNOWN,
                    technicalDebugMessage = "Expected $expectedType response, but got $responseApplicationType"
                )
            }
        } else {
            throw OslonokkelenClientException(
                traceId = oslonokkelenTraceId,
                errorCode = KeychainPushApiV2.ErrorResponse.ErrorCode.UNKNOWN,
                technicalDebugMessage = "Unsuccessful http response without details: ${httpResponse.status}"
            )
        }
    }


    private suspend fun handleStructuredErrorResponse(httpResponse: HttpResponse, oslonokkelenTraceId: String?) {
        val bytes = httpResponse.readBytes()
        val error = KeychainPushApiV2.ErrorResponse.parseFrom(bytes)

        throw OslonokkelenClientException(
            traceId = oslonokkelenTraceId,
            technicalDebugMessage = error.technicalDebugMessage,
            errorCode = error.errorCode
        )
    }

    private fun getApplicationResponseType(httpResponse: HttpResponse, oslonokkelenTraceId: String?): String? {
        val responseContentType = httpResponse.contentType()
            ?: throw OslonokkelenClientException(
                traceId = oslonokkelenTraceId,
                errorCode = KeychainPushApiV2.ErrorResponse.ErrorCode.INVALID_RESPONSE,
                technicalDebugMessage = "No content type in http response"
            )

        if (responseContentType.withoutParameters() != ContentType.Application.ProtoBuf) {
            throw OslonokkelenClientException(
                traceId = oslonokkelenTraceId,
                errorCode = KeychainPushApiV2.ErrorResponse.ErrorCode.INVALID_RESPONSE,
                technicalDebugMessage = "Expected protobuf response, not $responseContentType"
            )
        }

        return responseContentType.parameter("type")
    }

}