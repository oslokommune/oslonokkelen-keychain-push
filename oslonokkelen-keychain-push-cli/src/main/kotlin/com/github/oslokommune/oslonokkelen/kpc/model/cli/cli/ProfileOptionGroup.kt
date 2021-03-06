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

class ProfileOptionGroup(private val configurationHandle: ConfigurationHandle) : OptionGroup(
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

}