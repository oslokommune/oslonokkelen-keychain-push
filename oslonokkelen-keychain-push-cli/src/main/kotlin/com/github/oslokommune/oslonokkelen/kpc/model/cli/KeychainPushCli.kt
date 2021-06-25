package com.github.oslokommune.oslonokkelen.kpc.model.cli

import com.github.ajalt.clikt.core.subcommands
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.KeychainInfoCliCommand
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.KeychainPushCliCommand

fun main(args: Array<String>) {
    KeychainCliCommand()
        .subcommands(KeychainPushCliCommand(), KeychainInfoCliCommand())
        .main(args)
}

