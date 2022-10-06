package com.github.oslokommune.oslonokkelen.push.ktor

import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.push.OslonokkelenPushClient
import com.github.oslokommune.oslonokkelen.push.PushRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType

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

}