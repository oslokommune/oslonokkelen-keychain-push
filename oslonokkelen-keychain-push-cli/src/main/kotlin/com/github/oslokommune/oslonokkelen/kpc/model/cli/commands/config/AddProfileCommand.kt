package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle

class AddProfileCommand(
    private val out: CliOutput,
    private val configurationHandle: ConfigurationHandle
) : CliktCommand(
    help = "Add new third party system profile. Get in touch with the Oslonøkkelen team to get the a system id and api key.",
    name = "add"
) {

    private val backendUri by option(
        "--backend-uri",
        help = "Oslonøkkelen backend uri"
    ).default("https://oslonokkelen-backend-api.k8s.oslo.kommune.no")

    private val systemId by option("--system-id", help = "Your system id").required()
    private val apiSecret by option("--api-secret", help = "Your secret api key").required()

    override fun run() {
        out.stderr("Adding profile for $systemId at $backendUri")

        configurationHandle.addProfile(
            systemId = systemId,
            apiSecret = apiSecret,
            backendUri = backendUri
        )
    }
}
