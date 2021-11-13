package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.ProfileOptionGroup
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import io.ktor.client.HttpClient

class KeychainInfoCliCommand(
    private val out: CliOutput,
    configurationHandle: ConfigurationHandle,
    httpClient: HttpClient
) : CliktCommand(
    help = "Fetch keychain factory details",
    name = "info"
) {

    private val profileOptions by ProfileOptionGroup(configurationHandle, httpClient)
    private val keychainFactoryId by option("--keychain-factory-id", help = "The keychain factory you want to pull information for").required()

    override fun run() {
        out.print("Fetching keychain information for: $keychainFactoryId")

        profileOptions.withSession() { pushClient ->
            val factoryId = KeychainFactoryId(keychainFactoryId)
            val info = pushClient.pullFactoryInfo(factoryId)

            out.table {
                headers("Factory id", "Timezone")
                row(factoryId.value, info.timezone.id)
            }
        }
    }

}