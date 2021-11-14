package com.github.oslokommune.oslonokkelen.kpc.model.cli.cli

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.choice
import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.ktor.OslonokkelenKeychainPushKtorClient
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.Configuration
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import java.net.URI

class ProfileOptionGroup(
    private val configurationHandle: ConfigurationHandle,
    private val httpClient: HttpClient
) : OptionGroup(
    name = "Profile options",
    help = "Selects target profile (environment)"
) {

    val profileId by option(
        "--profile-id",
        help = "The profile / system you want to use",
        completionCandidates = CompletionCandidates.Custom.fromStdout("keychain-pusher auto --autocomplete PROFILE_IDS")
    )
        .choice(configurationHandle.profileIds.associateBy { it })
        .required()
        .validate { input ->
            if (!configurationHandle.profileIds.contains(input)) {
                if (configurationHandle.profileIds.isNotEmpty()) {
                    fail("Unknown profile '$input' (known profiles: ${configurationHandle.profileIds.joinToString(", ")})")
                } else {
                    fail("Unknown profile '$input'. See keychain-pusher config --help for information on how to register a new profile.")
                }
            }
        }


    fun withSession(block: suspend (OslonokkelenKeychainPushClient) -> Unit) {
        val profile: Configuration.Profile = configurationHandle.requireProfile(profileId)
        withClient(profile, httpClient, block)
    }

    companion object {
        fun withClient(
            profile: Configuration.Profile,
            httpClient: HttpClient,
            block: suspend (OslonokkelenKeychainPushClient) -> Unit
        ) {
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

}