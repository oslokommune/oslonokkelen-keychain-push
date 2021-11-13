package com.github.oslokommune.oslonokkelen.kpc.model.cli

import com.github.ajalt.clikt.core.subcommands
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.KeychainCliCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.AddProfileCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.ConfigCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.ListProfilesCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.config.RemoveProfileCommand
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
    val configurationHandle = ConfigProvider.readConfig(out)
    val httpClient = HttpClient(CIO)

    KeychainCliCommand()
        .subcommands(
            KeychainCommand().subcommands(
                KeychainPushCliCommand(out, configurationHandle, httpClient)
            ),
            KeychainFactoryCommand().subcommands(
                KeychainInfoCliCommand(out, configurationHandle, httpClient),
                ListKeychainFactoriesCommand(out, configurationHandle, httpClient)
            ),
            ConfigCommand().subcommands(
                AddProfileCommand(out, configurationHandle),
                ListProfilesCommand(out, configurationHandle),
                RemoveProfileCommand(out, configurationHandle, httpClient),
            )
        )
        .main(args)
}

