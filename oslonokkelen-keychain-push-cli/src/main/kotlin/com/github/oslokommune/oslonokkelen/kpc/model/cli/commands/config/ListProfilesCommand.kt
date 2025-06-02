package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle

class ListProfilesCommand(
    private val out: CliOutput,
    private val configurationHandle: ConfigurationHandle
) : CliktCommand(
    name = "ls"
) {

    override fun help(context: Context): String {
        return "List known profiles"
    }

    override fun run() {
        echo("Listing configured profiles...")

        out.table {
            headers("Profile id (system-id @ backend-uri)", "Active", "Secret")

            for (profileId in configurationHandle.profileIds) {
                val profile = configurationHandle.requireProfile(profileId)
                val isActive = profileId == configurationHandle.activeProfileId
                val activeText = if (isActive) {
                    "Yes"
                } else {
                    "No"
                }

                row(profile.id, activeText, profile.apiSecret)
            }
        }
    }
}