package com.github.oslokommune.oslonokkelen.kpc.model.cli

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
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories.KeychainFactoryCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories.KeychainInfoCliCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories.ListKeychainFactoriesCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.keychains.KeychainCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.keychains.KeychainPushCliCommand
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
            KeychainCommand().subcommands(
                KeychainPushCliCommand(out, service)
            ),
            KeychainFactoryCommand().subcommands(
                KeychainInfoCliCommand(out, service),
                ListKeychainFactoriesCommand(out, service)
            ),
            ConfigCommand().subcommands(
                AddProfileCommand(out, configurationHandle),
                ListProfilesCommand(out, configurationHandle),
                RemoveProfileCommand(out, configurationHandle, httpClient),
                CurrentProfileCommand(out, configurationHandle),
                UseProfileCommand(out, configurationHandle, httpClient)
            ),
            AutocompleteCommand(configurationHandle)
        )
        .main(args)
}