package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories

import com.github.ajalt.clikt.core.CliktCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliService

class ListKeychainFactoriesCommand(
    private val out: CliOutput,
    private val cliService: CliService
) : CliktCommand(
    help = "List keychain factories",
    name = "ls"
) {


    override fun run() {
        echo("Listing keychain factories...")

        cliService.withSession { client ->
            val factories = client.listFactories()

            if (factories.isNotEmpty()) {
                out.table {
                    headers("Id", "Title")

                    for (factory in factories.sortedBy { it.id.value }) {
                        row(factory.id.value, factory.title)
                    }
                }
            } else {
                echo("No keychain factories found", err = true)
            }
        }
    }

}