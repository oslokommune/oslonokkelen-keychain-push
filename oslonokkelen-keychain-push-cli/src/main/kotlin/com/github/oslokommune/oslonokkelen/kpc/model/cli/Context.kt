package com.github.oslokommune.oslonokkelen.kpc.model.cli

import com.github.oslokommune.oslonokkelen.kpc.ktor.OslonokkelenKeychainPushKtorClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId

class Context(
    val keychainFactoryId: KeychainFactoryId,
    val pushClient: OslonokkelenKeychainPushKtorClient
)