package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context

class ConfigCommand : CliktCommand(
    name = "config"
) {

    override fun help(context: Context): String {
        return "Cli config (this tool)"
    }

    override fun run() {
    }
}
