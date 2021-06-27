package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.push

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest
import com.github.oslokommune.oslonokkelen.kpc.model.cli.Context
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.MINUTES

class KeychainPushCliCommand : CliktCommand(
    help = "Push a keychain",
    name = "push"
) {

    private val now = LocalDateTime.now().truncatedTo(MINUTES)

    private val keychainIdStr by option("--keychain-id", help = "Identifies the keychain within the factory").required()
    private val title by option("--title", help = "Human readable title").required()
    private val recipientPhoneNumber by option("--recipient-phone-number", help = "Recipient phone number").required()
    private val from by option("--from", help = "From").default(now.toString())
    private val until by option("--until", help = "Until").default(now.plusDays(2).toString())

    private val context by requireObject<Context>()

    override fun run() {
        println("Pushing keychain")

        runBlocking {
            val info = context.pushClient.pullFactoryInfo(context.keychainFactoryId)
            val keychainId = info.id.createKeychainId(keychainIdStr)

            context.pushClient.push(keychainId, KeychainPushRequest.build(title) {
                information("No information for you")
                accessBetween(from, until)
                recipientByPhoneNumber("47", recipientPhoneNumber)
            })
        }

    }

}