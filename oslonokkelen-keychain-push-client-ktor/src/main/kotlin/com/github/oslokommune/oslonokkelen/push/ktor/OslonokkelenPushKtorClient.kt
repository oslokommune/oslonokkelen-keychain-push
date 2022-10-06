package com.github.oslokommune.oslonokkelen.push.ktor

import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.push.OslonokkelenClientException
import com.github.oslokommune.oslonokkelen.push.OslonokkelenPushClient
import com.github.oslokommune.oslonokkelen.push.PushRequest
import com.github.oslokommune.oslonokkelen.push.SystemInfo
import com.github.oslokommune.oslonokkelen.push.proto.KeychainPushApiV2
import io.ktor.client.HttpClient
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OslonokkelenPushKtorClient(
    val client: HttpClient,
    val config: OslonokkelenKeychainPushClient.Config
) : OslonokkelenPushClient {

    private val requestBuilder: HttpRequestBuilder.() -> Unit = {
        header(OslonokkelenKeychainPushClient.clientIdHeaderName, config.systemId)
        header(OslonokkelenKeychainPushClient.clientApiKeyHeaderName, config.apiSecret)
        accept(ContentType.Application.ProtoBuf)
        expectSuccess = false
    }

    override suspend fun push(request: PushRequest) {
        TODO("Æ har ikke blitt implementert!!")
    }

    override suspend fun describeSystem(): SystemInfo {
        TODO("Æ har ikke blitt implementert!!")
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