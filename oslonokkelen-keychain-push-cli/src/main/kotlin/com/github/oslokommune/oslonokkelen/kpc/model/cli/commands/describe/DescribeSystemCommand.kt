package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.describe

import com.github.ajalt.clikt.core.CliktCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliService

class DescribeSystemCommand(
    private val out: CliOutput,
    private val cliService: CliService
) : CliktCommand(
    help = "Describe your system",
    name = "describe"
) {

    override fun run() {
        cliService.withNewSession { pushClient ->
            out.debug("Describing system from ${profile.backendUri}")
            val description = pushClient.describeSystem()

            echo("")
            echo("Name:         ${description.name}")
            echo("System id:    ${description.id}")
            echo("Information:  ${description.information}")

            if (description.assetIds.isNotEmpty()) {
                echo("\nAsset ids:")
                for (assetId in description.assetIds) {
                    echo(" - ${assetId.id}")
                }
            } else {
                echo("No asset ids")
            }
        }
    }

}