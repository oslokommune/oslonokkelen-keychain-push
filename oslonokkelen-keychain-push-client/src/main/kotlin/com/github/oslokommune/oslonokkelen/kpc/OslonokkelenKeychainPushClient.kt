package com.github.oslokommune.oslonokkelen.kpc

import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryInfo
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest

interface OslonokkelenKeychainPushClient {

    suspend fun pullFactoryInfo(factoryId: String) : KeychainFactoryInfo

    suspend fun push(request: KeychainPushRequest)

}