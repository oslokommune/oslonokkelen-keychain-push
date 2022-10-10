package com.github.oslokommune.oslonokkelen.kpc.model.cli.config

import com.charleskorn.kaml.Yaml
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.text.Charsets.UTF_8

object ConfigProvider {

    const val filename = "keychain-pusher.yaml"

    fun readConfig(out: CliOutput, folder: Path = defaultConfigFolder()): ConfigurationHandle {
        val path = folder.resolve(filename)

        val config = if (!Files.exists(path)) {
            out.stderr("Configuration file not found.")
            out.stderr("Generating default configuration file: $path")
            val config = Configuration.default
            write(path, config)
            config
        } else {
            try {
                val yaml = Files.readString(path, UTF_8)
                Yaml.default.decodeFromString(Configuration.serializer(), yaml)
            } catch (ex: Exception) {
                throw IllegalStateException("Failed to read configuration from: $path", ex)
            }
        }

        return ConfigurationHandle(config, path)
    }

    internal fun write(path: Path, config: Configuration) {
        val yaml = Yaml.default.encodeToString(Configuration.serializer(), config)
        Files.write(path, yaml.toByteArray(UTF_8))
    }

    private fun defaultConfigFolder(): Path {
        val userHome = System.getProperty("user.home")
        return Paths.get(userHome, ".config/oslonokkelen")
    }
}
