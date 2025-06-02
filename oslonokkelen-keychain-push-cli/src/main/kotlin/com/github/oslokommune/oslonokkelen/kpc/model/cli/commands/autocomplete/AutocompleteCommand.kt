package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.autocomplete

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.enum
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliService
import com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.autocomplete.AutocompleteCommand.Autocomplete.PROFILE_IDS

class AutocompleteCommand(
    private val service: CliService
) : CliktCommand(
    name = "auto"
) {

    override fun help(context: Context): String {
        return "Autocomplete"
    }

    private val autocomplete: Autocomplete by option()
        .enum<Autocomplete>()
        .required()

    override fun run() {
        try {
            val values = findAutocompleteValuesFor()
            println(values.joinToString(" "))
        } catch (t: Throwable) {
            echo("Failed to generate autocomplete for $autocomplete: ${t.message}", err = true)
        }
    }

    private fun findAutocompleteValuesFor(): Set<String> {
        return when (autocomplete) {
            PROFILE_IDS -> {
                service.profileIds
            }
        }
    }

    enum class Autocomplete {
        PROFILE_IDS
    }

}