package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories

import com.github.ajalt.clikt.core.CliktCommand

class KeychainFactoryCommand : CliktCommand(
    help = "Keychain factories (manages user keychains)",
    name = "factory"
) {
    override fun run() {
    }
}
