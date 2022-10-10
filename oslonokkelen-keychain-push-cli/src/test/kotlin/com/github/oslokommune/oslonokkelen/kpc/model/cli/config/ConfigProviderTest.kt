package com.github.oslokommune.oslonokkelen.kpc.model.cli.config

import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliException
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

internal class ConfigProviderTest {

    @TempDir
    private lateinit var tempDir: Path;

    @Test
    fun `Will create default config file if missing`() {
        val configuration = readConfiguration()
        val expectedPath = tempDir.resolve(ConfigProvider.filename)

        assertThat(configuration).isNotNull
        assertThat(expectedPath).isRegularFile
    }

    @Test
    fun `Initial configuration will be empty`() {
        val configuration = readConfiguration()

        assertThat(configuration.profileIds).isEmpty()
        assertThat(configuration.activeProfileId).isNull()
    }

    @Test
    fun `First profile will be made default`() {
        val configuration = readConfiguration()

        configuration.addProfile(
            systemId = "test-system",
            apiSecret = "don't-tell-anyone",
            backendUri = "https://backend.example.com",
            grpcUri = "https://backend.example.com"
        )

        assertThat(configuration.activeProfileId).isEqualTo("test-system@https://backend.example.com")
    }

    @Test
    fun `Second profile will be made default`() {
        val configuration = readConfiguration()

        configuration.addProfile(
            systemId = "test-system",
            apiSecret = "don't-tell-anyone",
            backendUri = "https://backend.example.com",
            grpcUri = "https://backend.example.com"
        )

        configuration.addProfile(
            systemId = "test-system-2",
            apiSecret = "don't-tell-anyone",
            backendUri = "https://backend.example.com",
            grpcUri = "https://backend.example.com"
        )

        assertThat(configuration.activeProfileId).isEqualTo("test-system-2@https://backend.example.com")
    }

    @Test
    fun `Can swap active profile`() {
        val configuration = readConfiguration()

        configuration.addProfile(
            systemId = "test-system",
            apiSecret = "don't-tell-anyone",
            backendUri = "https://backend.example.com",
            grpcUri = "https://backend.example.com"
        )

        configuration.addProfile(
            systemId = "test-system-2",
            apiSecret = "don't-tell-anyone",
            backendUri = "https://backend.example.com",
            grpcUri = "https://backend.example.com"
        )

        configuration.useProfile("test-system@https://backend.example.com")

        assertThat(configuration.activeProfileId).isEqualTo("test-system@https://backend.example.com")
    }

    @Test
    fun `Trying to use missing profile results in exception`() {
        val configuration = readConfiguration()

        val ex = assertThrows<CliException> {
            configuration.useProfile("test-system@https://backend.example.com")
        }

        assertThat(ex).hasMessage("No profile with id: test-system@https://backend.example.com")
    }

    @Test
    fun `Removing hte last profile will also remove the active profile id`() {
        val configuration = readConfiguration()

        configuration.addProfile(
            systemId = "test-system",
            apiSecret = "don't-tell-anyone",
            backendUri = "https://backend.example.com",
            grpcUri = "https://backend.example.com"
        )

        configuration.removeProfile("test-system@https://backend.example.com")

        assertThat(configuration.activeProfileId).isNull()
        assertThat(configuration.profileIds).isEmpty()
    }

    @Test
    fun `Removing the second last profile will cause the one left to become active`() {
        val configuration = readConfiguration()

        configuration.addProfile(
            systemId = "test-system",
            apiSecret = "don't-tell-anyone",
            backendUri = "https://backend.example.com",
            grpcUri = "https://backend.example.com"
        )
        configuration.addProfile(
            systemId = "test-system-2",
            apiSecret = "don't-tell-anyone",
            backendUri = "https://backend.example.com",
            grpcUri = "https://backend.example.com"
        )

        configuration.removeProfile("test-system@https://backend.example.com")

        assertThat(configuration.activeProfileId).isEqualTo("test-system-2@https://backend.example.com")
        assertThat(configuration.profileIds).containsExactly("test-system-2@https://backend.example.com")
    }

    @Test
    fun `Adding the same system twice results in error`() {
        val configuration = readConfiguration()

        configuration.addProfile(
            systemId = "test-system",
            apiSecret = "don't-tell-anyone",
            backendUri = "https://backend.example.com",
            grpcUri = "https://backend.example.com"
        )

        val ex = assertThrows<CliException>() {
            configuration.addProfile(
                systemId = "test-system",
                apiSecret = "don't-tell-anyone",
                backendUri = "https://backend.example.com",
                grpcUri = "https://backend.example.com"
            )
        }

        assertThat(ex).hasMessage("Already have profile for system test-system @ https://backend.example.com")
    }

    private fun readConfiguration(): ConfigurationHandle {
        val cli = CliOutput()
        return ConfigProvider.readConfig(cli, tempDir)
    }

}