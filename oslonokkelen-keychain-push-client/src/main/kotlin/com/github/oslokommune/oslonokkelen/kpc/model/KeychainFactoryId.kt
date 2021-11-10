package com.github.oslokommune.oslonokkelen.kpc.model

/**
 * Identifies a keychain factory. Keychain factories are responsible for
 * creating personal keychains in Oslonøkkelen. Different factories can
 * create keychains granting users access to different doors.
 */
data class KeychainFactoryId(val value: String) {
    init {
        Id.validate(value)
    }

    fun createKeychainId(id: String): KeychainId {
        return KeychainId(this, id)
    }
}