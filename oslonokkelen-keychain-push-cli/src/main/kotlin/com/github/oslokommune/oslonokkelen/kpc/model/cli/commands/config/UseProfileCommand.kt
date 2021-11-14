package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.ProfileOptionGroup
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import io.ktor.client.HttpClient

class UseProfileCommand(private val configurationHandle: ConfigurationHandle) : CliktCommand(
    help = "Select profile",
    name = "use"
) {

    private val profileOptions by ProfileOptionGroup(configurationHandle)

    override fun run() {
        echo("Selecting profile ${profileOptions.profileId}")
        configurationHandle.useProfile(profileOptions.profileId)
    }
}
