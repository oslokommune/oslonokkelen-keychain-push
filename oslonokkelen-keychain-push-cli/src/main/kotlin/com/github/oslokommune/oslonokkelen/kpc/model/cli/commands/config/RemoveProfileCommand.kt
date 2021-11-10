package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle

class RemoveProfileCommand(
    private val out: CliOutput,
    private val configurationHandle: ConfigurationHandle
) : CliktCommand(
    help = "Remove local profile",
    name = "rm"
) {

    private val profileId by option("--profile-id", help = "Identifies the profile to delete").required()

    override fun run() {
        out.info("Removing profile $profileId")
        configurationHandle.removeProfile(profileId)
    }
}
