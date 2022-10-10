package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.meta

import com.github.ajalt.clikt.core.CliktCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliService

class DescribeSystemCommand(private val cliService: CliService) : CliktCommand(
    help = "Describe your system",
    name = "describe"
) {


    override fun run() {
        cliService.withSession { pushClient ->
            echo("Describing system from ${profile.backendUri}")


        }
    }

}