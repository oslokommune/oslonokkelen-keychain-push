package com.github.oslokommune.oslonokkelen.kpc.model.cli.cli

import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.Configuration
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import com.github.oslokommune.oslonokkelen.push.OslonokkelenClientConfig
import com.github.oslokommune.oslonokkelen.push.OslonokkelenPushClient
import com.github.oslokommune.oslonokkelen.push.ktor.OslonokkelenPushKtorClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import java.net.URI

class CliService(
    private val configurationHandle: ConfigurationHandle,
    private val httpClient: HttpClient
) {

    val profileIds: Set<String>
        get() = configurationHandle.profileIds


    fun <R> withNewSession(block: suspend Session.(OslonokkelenPushClient) -> R): R {
        val activeProfileId = configurationHandle.activeProfileId ?: throw CliException(
            """No profiles defined. 
            |See how you can add one by running 
            |
            |```
            |keychain-pusher config --help
            |```""".trimMargin()
        )

        val profile: Configuration.Profile = configurationHandle.requireProfile(activeProfileId)
        val client = OslonokkelenPushKtorClient(
            client = httpClient,
            config = OslonokkelenClientConfig(
                baseUri = URI.create(profile.backendUri),
                systemId = profile.systemId,
                apiSecret = profile.apiSecret
            )
        )

        return runBlocking {
            val session = Session(profile)
            block(session, client)
        }
    }

    class Session(val profile: Configuration.Profile)

}