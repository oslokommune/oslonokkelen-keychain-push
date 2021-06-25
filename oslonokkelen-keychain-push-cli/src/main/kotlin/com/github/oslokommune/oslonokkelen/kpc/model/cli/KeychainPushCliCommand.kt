package com.github.oslokommune.oslonokkelen.kpc.model.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import kotlinx.coroutines.runBlocking

class KeychainPushCliCommand : CliktCommand(
    help = "Push a keychain",
    name = "push"
) {

    private val client by requireObject<OslonokkelenKeychainPushClient>()
    private val keychainFactoryId by requireObject<KeychainFactoryId>()

    override fun run() {
        println("Pushing keychain")

        runBlocking {
            val info = client.pullFactoryInfo(keychainFactoryId)
            println(info)
        }

    }

}