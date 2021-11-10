package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.info

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.cli.Context
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ProfileSelector
import kotlinx.coroutines.runBlocking

class KeychainInfoCliCommand(private val out: CliOutput) : CliktCommand(
    help = "Fetch keychain factory details",
    name = "info"
) {

    private val selectedProfile by requireObject<ProfileSelector>()
    private val profileId by option("--profile-id", help = "The profile / system you want to use").required()
    private val keychainFactoryId by option("--keychain-factory-id", help = "The keychain factory you want to pull information for").required()

    override fun run() {
        println("Fetching keychain information for: $keychainFactoryId")

        selectedProfile.withSession(profileId) { pushClient ->
            val factoryId = KeychainFactoryId(keychainFactoryId)
            val info = pushClient.pullFactoryInfo(factoryId)

            out.table {
                headers("Factory id", "Timezone")
                row(factoryId.value, info.timezone.id)
            }
        }
    }

}