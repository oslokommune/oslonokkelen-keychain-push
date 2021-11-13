package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import io.ktor.client.HttpClient

class AddProfileCommand(
    private val out: CliOutput,
    private val configurationHandle: ConfigurationHandle
) : CliktCommand(
    help = "Add new third party system profile",
    name = "add"
) {

    private val profileId by option("--profile-id", help = "Identifies this profile for later use").required()
    private val backendUri by option(
        "--backend-uri",
        help = "Oslonøkkelen backend uri"
    ).default("https://oslonokkelen-backend-api.k8s.oslo.kommune.no")

    private val systemId by option("--system-id", help = "Your system id").required()
    private val apiSecret by option("--api-secret", help = "Your secret api key").required()

    override fun run() {
        out.print("Adding profile for $systemId at $backendUri")

        configurationHandle.addProfile(
            profileId = profileId,
            systemId = systemId,
            apiSecret = apiSecret,
            backendUri = backendUri
        )
    }
}
