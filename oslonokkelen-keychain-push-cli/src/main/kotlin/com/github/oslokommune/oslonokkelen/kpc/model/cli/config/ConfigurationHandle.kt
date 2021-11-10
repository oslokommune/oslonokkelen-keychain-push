package com.github.oslokommune.oslonokkelen.kpc.model.cli.config

import java.nio.file.Path

class ConfigurationHandle(
    private var configuration: Configuration,
    private val path: Path
) {

    val profileIds: Set<String>
        get() = configuration.profiles.map { it.id }.toSet()

    fun removeProfile(profileId: String) {
        mutateConfiguration {
            configuration.copy(profiles = configuration.profiles.filterNot { it.id == profileId })
        }
    }

    fun addProfile(profileId: String, systemId: String, apiSecret: String, backendUri: String) {
        if (configuration.profiles.any { it.id == profileId }) {
            throw IllegalStateException("Already have profile with id: $profileId")
        }

        val newProfile = Configuration.Profile(
            id = profileId,
            systemId = systemId,
            apiSecret = apiSecret,
            backendUri = backendUri
        )

        mutateConfiguration {
            configuration.copy(profiles = configuration.profiles + newProfile)
        }
    }

    fun requireProfile(id: String): Configuration.Profile {
        return configuration.profiles.firstOrNull { it.id == id } ?: throw IllegalStateException("No profile: $id")
    }

    fun replaceProfile(updatedProfile: Configuration.Profile): Configuration.Profile {
        val other = configuration.profiles.filterNot { it.id == updatedProfile.id }

        mutateConfiguration {
            configuration.copy(
                profiles = other + updatedProfile
            )
        }

        return updatedProfile
    }

    private fun mutateConfiguration(block: () -> Configuration) {
        val updatedConfiguration = block()
        ConfigProvider.write(path, updatedConfiguration)
        configuration = updatedConfiguration
    }
}
