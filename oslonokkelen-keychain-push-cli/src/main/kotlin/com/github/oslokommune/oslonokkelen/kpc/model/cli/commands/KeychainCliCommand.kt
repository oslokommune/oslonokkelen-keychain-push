package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.MordantHelpFormatter

class KeychainCliCommand : CliktCommand(
    name = "keychain-pusher"
) {

    override fun help(context: Context): String {
        return """A tool to simulate what Oslonøkkelen calls a "third party system" intended for experimenting / debugging. 
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
    """.trimMargin()
    }

    init {
        context {
            helpFormatter = { context ->
                MordantHelpFormatter(
                    showDefaultValues = true,
                    showRequiredTag = true,
                    context = context
                )
            }
        }
    }

    override fun run() {
    }
}