package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.keychains

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.ProfileOptionGroup
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ConfigurationHandle
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ProfileSelector
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.MINUTES

class KeychainPushCliCommand(
    private val out: CliOutput,
    private val configurationHandle: ConfigurationHandle
) : CliktCommand(
    help = "Push a keychain",
    name = "push"
) {

    private val now = LocalDateTime.now().truncatedTo(MINUTES)
    private val selectedProfile by requireObject<ProfileSelector>()

    private val profileOptions by ProfileOptionGroup(configurationHandle)

    private val keychainFactoryIdStr by option("--keychain-factory-id", help = "Identifies the keychain factory. This will factory will decide what the keychain can unlock.").required()
    private val keychainIdStr by option("--keychain-id", help = "Identifies the keychain within a factory. This could be something like a booking reference.").required()
    private val title by option("--title", help = "Human readable title. Ideally something that makes the user remember what this is").required()
    private val recipientPhoneNumber by option("--recipient-phone-number", help = "Recipient norwegian phone number (eight digits)").required()
    private val from by option("--from", help = "From (example: $now)").required()
    private val until by option("--until", help = "Until (example: ${now.plusDays(2)})").required()

    override fun run() {
        out.print("Pushing keychain $keychainFactoryIdStr/$keychainIdStr")

        selectedProfile.withSession(profileOptions.profileId) { pushClient ->
            val info = pushClient.pullFactoryInfo(KeychainFactoryId(keychainFactoryIdStr))
            val keychainId = info.id.createKeychainId(keychainIdStr)

            pushClient.push(keychainId, KeychainPushRequest.build(title) {
                information("No information for you")
                accessBetween(from, until)
                recipientByPhoneNumber("47", recipientPhoneNumber)
            })
        }
    }

}