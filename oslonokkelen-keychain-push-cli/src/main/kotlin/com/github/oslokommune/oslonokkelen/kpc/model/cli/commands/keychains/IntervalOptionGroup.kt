package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.keychains

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import com.github.oslokommune.oslonokkelen.kpc.model.cli.time.DateTimeParser
import java.time.LocalDateTime

class IntervalOptionGroup : OptionGroup(
    name = "Interval",
    help = """The interval the keychain should be valid within.
        |
        | Examples:
        |```
        |   > 2021-11-01T10:00:00
        |   > now+2h
        |   > now+2d-1h
        |```
    """.trimMargin()
) {

    val from by option("--from", help = "From")
        .convert { dateTimeParser.parse(it) }
        .required()

    val until by option("--until", help = "Until")
        .convert { dateTimeParser.parse(it) }
        .required()
        .validate {
            if (it <= from) {
                fail("Keychain can't expire before it is valid")
            }
        }


    companion object {

        private val dateTimeParser = DateTimeParser.newParser(LocalDateTime.now())

    }

}