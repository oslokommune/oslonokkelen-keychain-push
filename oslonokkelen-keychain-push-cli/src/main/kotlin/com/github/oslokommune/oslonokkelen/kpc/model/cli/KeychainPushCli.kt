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
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.keychains.KeychainDeleteCliCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.keychains.KeychainPushCliCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.meta.DescribeSystemCommand
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
                KeychainPushCliCommand(service),
                KeychainDeleteCliCommand(service)
            ),
            KeychainFactoryCommand().subcommands(
                KeychainInfoCliCommand(out, service),
                ListKeychainFactoriesCommand(out, service)
            ),
            ConfigCommand().subcommands(
                AddProfileCommand(configurationHandle),
                ListProfilesCommand(out, configurationHandle),
                RemoveProfileCommand(configurationHandle),
                CurrentProfileCommand(configurationHandle),
                UseProfileCommand(configurationHandle)
            ),
            DescribeSystemCommand(service),
            AutocompleteCommand(service)
        )
        .main(args)
}