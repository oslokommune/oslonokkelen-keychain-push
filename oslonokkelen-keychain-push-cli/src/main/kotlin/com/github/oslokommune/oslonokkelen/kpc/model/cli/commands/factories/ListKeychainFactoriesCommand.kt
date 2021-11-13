package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.ProfileOptionGroup
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ProfileSelector

class ListKeychainFactoriesCommand(
    private val out: CliOutput,
    configurationHandle: ConfigurationHandle
) : CliktCommand(
    help = "List keychain factories",
    name = "ls"
) {

    private val selectedProfile by requireObject<ProfileSelector>()
    private val profileOptions by ProfileOptionGroup(configurationHandle)

    override fun run() {
        out.print("Listing keychain factories...")

        selectedProfile.withSession(profileOptions.profileId) { client ->
            val factories = client.listFactories()

            if (factories.isNotEmpty()) {
                out.table {
                    headers("Id", "Title")

                    for (factory in factories.sortedBy { it.id.value }) {
                        row(factory.id.value, factory.title)
                    }
                }
            } else {
                out.print("No keychain factories found")
            }
        }
    }

}