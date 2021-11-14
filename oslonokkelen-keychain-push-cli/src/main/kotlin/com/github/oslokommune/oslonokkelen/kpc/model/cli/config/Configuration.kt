package com.github.oslokommune.oslonokkelen.kpc.model.cli.config

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val activeProfileId : String? = null,
    val profiles: List<Profile> = emptyList()
) {

    @Serializable
    data class Profile(
        val systemId: String,
        val apiSecret: String,
        val backendUri: String
    ) {
        val id = "$systemId@$backendUri"
    }

    companion object {

        val default = Configuration()
    }
}
