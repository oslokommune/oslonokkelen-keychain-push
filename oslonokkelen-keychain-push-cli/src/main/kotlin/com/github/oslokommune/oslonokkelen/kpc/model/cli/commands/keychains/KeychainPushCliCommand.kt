package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.keychains

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliService
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories.KeychainFactoryIdOptionGroup

class KeychainPushCliCommand(private val cliService: CliService) : CliktCommand(
    help = "Push a keychain",
    name = "push"
) {

    private val keychainFactoryId by KeychainFactoryIdOptionGroup()

    private val keychainIdStr by option(
        "--keychain-id",
        help = "Identifies the keychain within a factory. This could be something like a booking reference."
    ).required()

    private val title by option(
        "--title",
        help = "Human readable title. Ideally something that makes the user remember what this is"
    ).required()

    private val recipientPhoneNumber by option(
        "--recipient-phone-number",
        help = "Recipient norwegian phone number (eight digits)"
    ).required()

    private val interval by IntervalOptionGroup()

    override fun run() {
        echo("Pushing keychain ${keychainFactoryId.id.value}/$keychainIdStr")

        cliService.withSession { pushClient ->
            val info = pushClient.pullFactoryInfo(keychainFactoryId.id)
            val keychainId = info.id.createKeychainId(keychainIdStr)

            pushClient.push(keychainId, KeychainPushRequest.build(title) {
                information("No information for you")
                accessBetween(interval.from, interval.until)
                recipientByPhoneNumber("47", recipientPhoneNumber)
            })
        }
    }

}

