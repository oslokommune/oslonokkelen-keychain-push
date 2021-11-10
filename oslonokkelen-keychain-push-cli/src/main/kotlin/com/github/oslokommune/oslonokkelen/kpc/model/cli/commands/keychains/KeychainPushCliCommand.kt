package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.keychains

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest
import com.github.oslokommune.oslonokkelen.kpc.model.cli.Context
import com.github.oslokommune.oslonokkelen.kpc.model.cli.config.ProfileSelector
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.MINUTES

class KeychainPushCliCommand : CliktCommand(
    help = "Push a keychain",
    name = "push"
) {

    private val now = LocalDateTime.now().truncatedTo(MINUTES)
    private val selectedProfile by requireObject<ProfileSelector>()

    private val profileId by option("--profile-id", help = "The profile / system you want to use").required()

    private val keychainFactoryIdStr by option("--keychain-factory-id", help = "Identifies the keychain within the factory").required()
    private val keychainIdStr by option("--keychain-id", help = "Identifies the keychain within the factory").required()
    private val title by option("--title", help = "Human readable title").required()
    private val recipientPhoneNumber by option("--recipient-phone-number", help = "Recipient phone number").required()
    private val from by option("--from", help = "From (example: $now)").required()
    private val until by option("--until", help = "Until (example: ${now.plusDays(2)})").required()

    override fun run() {
        println("Pushing keychain")

        selectedProfile.withSession(profileId) { pushClient ->
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