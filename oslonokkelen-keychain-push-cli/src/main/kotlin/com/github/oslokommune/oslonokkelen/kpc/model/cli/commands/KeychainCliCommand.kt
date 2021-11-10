package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.ktor.OslonokkelenKeychainPushKtorClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.cli.Context
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ProfileSelector
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import java.net.URI

class KeychainCliCommand(
    private val httpClient: HttpClient,
    private val configurationHandle: ConfigurationHandle
) : CliktCommand(
    help = "Simulated third party system",
    name = "keychain-pusher"
) {
    override fun run() {
        currentContext.findOrSetObject {
            ProfileSelector(configurationHandle, httpClient)
        }
    }
}