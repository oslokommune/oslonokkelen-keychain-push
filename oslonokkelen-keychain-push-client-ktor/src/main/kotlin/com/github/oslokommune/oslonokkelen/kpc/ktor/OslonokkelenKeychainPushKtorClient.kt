package com.github.oslokommune.oslonokkelen.kpc.ktor

import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryInfo
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest

class OslonokkelenKeychainPushKtorClient : OslonokkelenKeychainPushClient {
    override suspend fun pullFactoryInfo(factoryId: String): KeychainFactoryInfo {
        TODO("Not yet implemented")
    }

    override suspend fun push(request: KeychainPushRequest) {
        TODO("Not yet implemented")
    }
}