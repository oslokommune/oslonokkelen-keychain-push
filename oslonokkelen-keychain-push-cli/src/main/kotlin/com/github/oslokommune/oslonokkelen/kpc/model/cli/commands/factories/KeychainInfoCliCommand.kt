package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliService

class KeychainInfoCliCommand(
    private val out: CliOutput,
    private val cliService: CliService
) : CliktCommand(
    help = "Fetch keychain factory details",
    name = "info"
) {

    private val factoryIdOptionsGroup by KeychainFactoryIdOptionGroup()

    override fun run() {
        echo("Fetching keychain information for: ${factoryIdOptionsGroup.id}")

        cliService.withSession { pushClient ->
            val info = pushClient.pullFactoryInfo(factoryIdOptionsGroup.id)

            out.table {
                headers("Factory id", "Timezone")
                row(factoryIdOptionsGroup.id.value, info.timezone.id)
            }
        }
    }

}