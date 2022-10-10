package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import io.ktor.client.HttpClient

class KeychainCliCommand : CliktCommand(
    help = """A tool to simulate what Oslonøkkelen calls a "third party system" intended for experimenting / debugging. 
        |In order to use this tool you need a few things from Oslonøkkelen: 
        | ```
        |  - A system id
        |  - An api secret
        |  - Uri to the api 
        | ```
        |
        | Once you have this information you can configure a profile. You can configure multiple
        | profiles for different environments (prod, test, local..).  
        | ```
        | keychain-pusher config add 
        |     --backend-uri <https://....> \
        |     --grpc-uri <https://....> \
        |     --system-id <your-system-id> \
        |     --api-secret <your-secret> 
        | ```
    """.trimMargin(),
    name = "keychain-pusher"
) {

    init {
        context {
            helpFormatter = CliktHelpFormatter(
                showDefaultValues = true,
                showRequiredTag = true
            )
        }
    }

    override fun run() {
    }
}