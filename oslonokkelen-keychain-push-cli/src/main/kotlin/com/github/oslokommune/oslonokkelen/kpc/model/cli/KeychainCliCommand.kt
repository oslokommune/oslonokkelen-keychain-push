package com.github.oslokommune.oslonokkelen.kpc.model.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.ktor.OslonokkelenKeychainPushKtorClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import java.net.URI

class KeychainCliCommand : CliktCommand(
    help = "Simulated third party system",
    name = "keychain-pusher"
) {

    private val backendUri by option("-u", "--uri", help = "Oslonøkkelen backend uri").required()
    private val clientId by option("-i", "--client-id", help = "Your client id").required()
    private val apiKey by option("-k", "--api-key", help = "Your secret api key").required()
    private val keychainFactoryId by option(
        "-kfi",
        "--keychain-factory-id",
        help = "Id of the keychain factory"
    ).required()


    override fun run() {
        currentContext.findOrSetObject {
            Context(
                keychainFactoryId = KeychainFactoryId(keychainFactoryId),
                pushClient = OslonokkelenKeychainPushKtorClient(
                    client = HttpClient(CIO),
                    config = OslonokkelenKeychainPushClient.Config(
                        baseUri = URI.create(backendUri),
                        clientId = clientId,
                        clientApiKey = apiKey
                    )
                )
            )
        }
    }

}