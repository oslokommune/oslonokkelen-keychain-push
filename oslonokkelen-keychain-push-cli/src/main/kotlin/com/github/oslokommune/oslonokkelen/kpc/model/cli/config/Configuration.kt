package com.github.oslokommune.oslonokkelen.kpc.model.cli.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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

        @Transient
        val id = "$systemId@$backendUri"
    }

    companion object {

        val default = Configuration()
    }
}
