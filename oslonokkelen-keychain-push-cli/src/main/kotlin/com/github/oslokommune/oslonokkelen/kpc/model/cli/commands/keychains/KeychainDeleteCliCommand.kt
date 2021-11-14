package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.keychains

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainDeleteRequest
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliService
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories.KeychainFactoryIdOptionGroup

class KeychainDeleteCliCommand(private val cliService: CliService) : CliktCommand(
    help = "Delete a keychain",
    name = "rm"
) {

    private val keychainFactoryId by KeychainFactoryIdOptionGroup()

    private val keychainIdStr by option(
        "--keychain-id",
        help = "Identifies the keychain within a factory. This could be something like a booking reference."
    ).required()

    private val reason by option(
        "--reason",
        help = "Why..."
    ).required()

    override fun run() {
        cliService.withSession { pushClient ->
            echo("Deleting keychain ${keychainFactoryId.id.value}/$keychainIdStr from ${profile.backendUri}")

            val keychainId = keychainFactoryId.id.createKeychainId(keychainIdStr)
            pushClient.delete(keychainId, KeychainDeleteRequest(reason))
        }
    }

}

