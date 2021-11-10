package com.github.oslokommune.oslonokkelen.kpc.model.cli.config

import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.ktor.OslonokkelenKeychainPushKtorClient
import kotlinx.coroutines.runBlocking
import java.net.URI

class ProfileSelector(
    private val configurationHandle: ConfigurationHandle,
    private val httpClient: io.ktor.client.HttpClient
) {

    fun withSession(id: String, block: suspend (OslonokkelenKeychainPushClient) -> Unit) {
        val profile = configurationHandle.requireProfile(id)

        val client = OslonokkelenKeychainPushKtorClient(
            client = httpClient,
            config = OslonokkelenKeychainPushClient.Config(
                baseUri = URI.create(profile.backendUri),
                systemId = profile.systemId,
                apiSecret = profile.apiSecret
            )
        )

        runBlocking {
            block(client)
        }
    }

}
