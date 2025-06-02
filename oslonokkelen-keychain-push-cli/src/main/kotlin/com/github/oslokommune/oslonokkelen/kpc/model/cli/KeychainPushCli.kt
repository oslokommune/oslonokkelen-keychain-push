package com.github.oslokommune.oslonokkelen.kpc.model.cli

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliService
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.KeychainCliCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.autocomplete.AutocompleteCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.AddProfileCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.ConfigCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.CurrentProfileCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.ListProfilesCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.RemoveProfileCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.UseProfileCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.describe.DescribeSystemCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.sync.SyncCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

fun main(args: Array<String>) {
    val out = CliOutput()
    val httpClient = HttpClient(CIO)
    val configurationHandle = ConfigProvider.readConfig(out)
    val service = CliService(configurationHandle, httpClient)

    KeychainCliCommand()
        .subcommands(
            ConfigCommand().subcommands(
                AddProfileCommand(configurationHandle),
                ListProfilesCommand(out, configurationHandle),
                RemoveProfileCommand(configurationHandle),
                CurrentProfileCommand(configurationHandle),
                UseProfileCommand(configurationHandle)
            ),
            DescribeSystemCommand(out, service),
            SyncCommand(out, service),
            AutocompleteCommand(service)
        )
        .main(args)
}