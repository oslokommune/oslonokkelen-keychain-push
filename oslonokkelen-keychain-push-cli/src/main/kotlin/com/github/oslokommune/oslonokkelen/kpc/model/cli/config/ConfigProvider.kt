package com.github.oslokommune.oslonokkelen.kpc.model.cli.config

import com.charleskorn.kaml.Yaml
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.text.Charsets.UTF_8

object ConfigProvider {

    fun readConfig(out: CliOutput): ConfigurationHandle {
        val path = defaultConfigFile()

        val config = if (!Files.exists(path)) {
            out.print("Configuration file not found.")
            out.print("Generating default configuration file: $path")
            val config = Configuration.default
            write(path, config)
            config
        } else {
            val yaml = Files.readString(path, UTF_8)
            Yaml.default.decodeFromString(Configuration.serializer(), yaml)
        }

        return ConfigurationHandle(config, path)
    }

    internal fun write(path: Path, config: Configuration) {
        val yaml = Yaml.default.encodeToString(Configuration.serializer(), config)
        Files.write(path, yaml.toByteArray(UTF_8))
    }

    private fun defaultConfigFile(): Path {
        val userHome = System.getProperty("user.home")
        return Paths.get(userHome, ".config/oslonokkelen/keychain-pusher.yaml")
    }
}
