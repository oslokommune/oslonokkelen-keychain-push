package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config

import com.github.ajalt.clikt.core.CliktCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import io.ktor.client.HttpClient

class CurrentProfileCommand(
    private val out: CliOutput,
    private val configurationHandle: ConfigurationHandle
) : CliktCommand(
    help = "Get current profile",
    name = "current"
) {

    override fun run() {
        val current = configurationHandle.activeProfileId

        if (current != null) {
            out.stdout(current)
        } else {
            out.stderr("No profiles")
        }
    }
}