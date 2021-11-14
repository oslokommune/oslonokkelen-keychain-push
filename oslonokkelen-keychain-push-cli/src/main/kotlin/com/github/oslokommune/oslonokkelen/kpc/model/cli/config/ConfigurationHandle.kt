package com.github.oslokommune.oslonokkelen.kpc.model.cli.config

import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliException
import java.nio.file.Path

class ConfigurationHandle(
    private var configuration: Configuration,
    private val path: Path
) {

    val profileIds: Set<String>
        get() = configuration.profiles.map { it.id }.toSet()

    val activeProfileId : String?
        get() = configuration.activeProfileId

    fun removeProfile(profileId: String) {
        mutateConfiguration {
            val filteredProfiles = configuration.profiles.filterNot { it.id == profileId }

            configuration.copy(
                profiles = filteredProfiles,
                activeProfileId = filteredProfiles.firstOrNull()?.id
            )
        }
    }

    fun addProfile(systemId: String, apiSecret: String, backendUri: String) {
        if (configuration.profiles.any { it.systemId == systemId && it.backendUri == backendUri }) {
            throw CliException("Already have profile for system $systemId @ $backendUri")
        }

        val newProfile = Configuration.Profile(
            backendUri = backendUri,
            apiSecret = apiSecret,
            systemId = systemId
        )

        mutateConfiguration {
            configuration.copy(
                profiles = configuration.profiles + newProfile,
                activeProfileId = newProfile.id
            )
        }
    }

    fun requireProfile(id: String): Configuration.Profile {
        return configuration.profiles.firstOrNull { it.id == id } ?: throw IllegalStateException("No profile: $id")
    }

    fun useProfile(id: String) {
        if (!profileIds.contains(id)) {
            throw CliException("No profile with id: $id")
        }

        mutateConfiguration {
            configuration.copy(
                activeProfileId = id
            )
        }
    }

    private fun mutateConfiguration(block: () -> Configuration) {
        val updatedConfiguration = block()
        ConfigProvider.write(path, updatedConfiguration)
        configuration = updatedConfiguration
    }
}
