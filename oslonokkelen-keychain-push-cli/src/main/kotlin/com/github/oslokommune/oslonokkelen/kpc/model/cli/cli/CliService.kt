package com.github.oslokommune.oslonokkelen.kpc.model.cli.cli

import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.ktor.OslonokkelenKeychainPushKtorClient
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.Configuration
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import java.net.URI

class CliService(
    private val configurationHandle: ConfigurationHandle,
    private val httpClient: HttpClient
) {

    val profileIds : Set<String>
        get() = configurationHandle.profileIds

    fun withSession(block: suspend (OslonokkelenKeychainPushClient) -> Unit) {
        val activeProfileId = configurationHandle.activeProfileId ?: throw CliException(
            """No profiles defined. 
            |See how you can add one by running 
            |
            |```
            |keychain-pusher config --help
            |```""".trimMargin()
        )

        val profile: Configuration.Profile = configurationHandle.requireProfile(activeProfileId)
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