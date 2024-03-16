package com.github.oslokommune.oslonokkelen.push.ktor

import com.github.oslokommune.oslonokkelen.push.AssetId
import com.github.oslokommune.oslonokkelen.push.OslonokkelenClientConfig
import com.github.oslokommune.oslonokkelen.push.OslonokkelenClientException
import com.github.oslokommune.oslonokkelen.push.OslonokkelenPushClient
import com.github.oslokommune.oslonokkelen.push.OslonokkelenPushClient.Companion.clientApiKeyHeaderName
import com.github.oslokommune.oslonokkelen.push.OslonokkelenPushClient.Companion.clientIdHeaderName
import com.github.oslokommune.oslonokkelen.push.OslonokkelenPushClient.Companion.traceIdHeaderName
import com.github.oslokommune.oslonokkelen.push.PermissionList
import com.github.oslokommune.oslonokkelen.push.PermissionListId
import com.github.oslokommune.oslonokkelen.push.PermissionState
import com.github.oslokommune.oslonokkelen.push.PermissionsIndex
import com.github.oslokommune.oslonokkelen.push.ProtoMarshaller
import com.github.oslokommune.oslonokkelen.push.SystemInfo
import com.github.oslokommune.oslonokkelen.push.proto.KeychainPushApiV2
import com.google.protobuf.GeneratedMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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
        header(clientIdHeaderName, config.systemId)
        header(clientApiKeyHeaderName, config.apiSecret)
        accept(ContentType.Application.ProtoBuf)
        expectSuccess = false
    }

    override suspend fun queryState(id: PermissionListId): PermissionState {
        return try {
            val httpResponse = client.get(config.stateUri(id)) {
                requestBuilder(this)
            }

            val message = readSuccessfulResponsePayload(
                factory = KeychainPushApiV2.StateResponse::parseFrom,
                httpResponse = httpResponse,
                expectedType = "state"
            )

            ProtoMarshaller.fromProtobuf(message)
        } catch (ex: OslonokkelenClientException) {
            throw ex
        } catch (ex: Exception) {
            throw OslonokkelenClientException(
                errorCode = KeychainPushApiV2.ErrorResponse.ErrorCode.UNKNOWN,
                technicalDebugMessage = "Unexpected error while fetching permission state",
                cause = ex
            )
        }
    }

    override suspend fun push(permissionList: PermissionList) {
        try {
            val httpResponse = client.post(config.pushUri) {
                setBody(ProtoMarshaller.toProtobuf(permissionList).toByteArray())
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

    override suspend fun index(): PermissionsIndex {
        return try {
            val httpResponse = client.get(config.systemIndexUri) {
                requestBuilder(this)
            }

            val protobufMessage = readSuccessfulResponsePayload(
                factory = KeychainPushApiV2.IndexResponse::parseFrom,
                httpResponse = httpResponse,
                expectedType = "index"
            )

            ProtoMarshaller.fromProtobuf(protobufMessage)
        } catch (ex: OslonokkelenClientException) {
            throw ex
        } catch (ex: Exception) {
            throw OslonokkelenClientException(
                errorCode = KeychainPushApiV2.ErrorResponse.ErrorCode.UNKNOWN,
                technicalDebugMessage = "Unexpected exception while looking up permission index",
                cause = ex
            )
        }
    }

    override suspend fun delete(id: PermissionListId) {
        try {
            val httpResponse = client.delete(config.deleteUri(id)) {
                requestBuilder(this)
            }

            readSuccessfulResponsePayload(
                factory = KeychainPushApiV2.DeleteResponse::parseFrom,
                httpResponse = httpResponse,
                expectedType = "delete"
            )
        } catch (ex: OslonokkelenClientException) {
            throw ex
        } catch (ex: Exception) {
            throw OslonokkelenClientException(
                errorCode = KeychainPushApiV2.ErrorResponse.ErrorCode.UNKNOWN,
                technicalDebugMessage = "Unexpected exception while looking up permission index",
                cause = ex
            )
        }
    }

    private suspend fun <M : GeneratedMessage> readSuccessfulResponsePayload(
        httpResponse: HttpResponse,
        expectedType: String,
        factory: (ByteArray) -> M
    ): M {
        val oslonokkelenTraceId = httpResponse.headers[traceIdHeaderName]
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