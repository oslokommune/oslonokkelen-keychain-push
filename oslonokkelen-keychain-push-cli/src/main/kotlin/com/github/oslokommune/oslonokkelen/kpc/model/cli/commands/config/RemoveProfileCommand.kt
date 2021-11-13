package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.ProfileOptionGroup
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import io.ktor.client.HttpClient

class RemoveProfileCommand(
    private val out: CliOutput,
    private val configurationHandle: ConfigurationHandle,
    httpClient: HttpClient
) : CliktCommand(
    help = "Remove local profile",
    name = "rm"
) {

    private val profileOptions by ProfileOptionGroup(configurationHandle, httpClient)

    override fun run() {
        out.print("Removing profile ${profileOptions.profileId}")
        configurationHandle.removeProfile(profileOptions.profileId)
    }
}
