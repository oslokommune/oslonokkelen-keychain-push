package com.github.oslokommune.oslonokkelen.kpc.model.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import kotlinx.coroutines.runBlocking

class KeychainInfoCliCommand : CliktCommand(
    help = "Fetch keychain factory details",
    name = "info"
) {

    private val context by requireObject<Context>()

    override fun run() {
        println("Fetching keychain information: ${context.keychainFactoryId.value}")

        runBlocking {
            val info = context.pushClient.pullFactoryInfo(context.keychainFactoryId)

            println(context.keychainFactoryId.value)
            println("=".repeat(context.keychainFactoryId.value.length))
            println("Timezone: ${info.timezone.id}")
        }

    }

}