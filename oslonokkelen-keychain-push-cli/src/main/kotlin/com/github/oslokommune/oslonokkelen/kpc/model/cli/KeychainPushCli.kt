package com.github.oslokommune.oslonokkelen.kpc.model.cli

import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    KeychainCliCommand()
        .subcommands(KeychainPushCliCommand(), KeychainInfoCliCommand())
        .main(args)
}

