package com.github.oslokommune.oslonokkelen.kpc.model.cli.config

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(val profiles: List<Profile> = emptyList()) {

    @Serializable
    data class Profile(
        val id: String,
        val systemId: String,
        val apiSecret: String,
        val backendUri: String
    )

    companion object {

        val default = Configuration()
    }
}
