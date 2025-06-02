package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.ProfileOptionGroup
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle

class RemoveProfileCommand(private val configurationHandle: ConfigurationHandle) : CliktCommand(
    name = "rm"
) {

    private val profileOptions by ProfileOptionGroup(configurationHandle)

    override fun help(context: Context): String {
        return "Remove local profile"
    }

    override fun run() {
        echo("Removing profile ${profileOptions.profileId}")
        configurationHandle.removeProfile(profileOptions.profileId)
    }
}
