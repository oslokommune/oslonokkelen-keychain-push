package com.github.oslokommune.oslonokkelen.kpc.model

import java.time.Duration
import java.time.LocalDateTime

data class Period(
        val from: LocalDateTime,
        val until: LocalDateTime
) {
    init {
        if (from >= until) {
            throw IllegalArgumentException("Invalid period (from >= until)")
        }

        val days = Duration.between(from, until).toDays()

        if (days > MAX_DAYS) {
            throw IllegalArgumentException("Can't grant access for more then $MAX_DAYS days")
        }
    }

    override fun toString(): String {
        return "$from - $until"
    }

    companion object {

        const val MAX_DAYS = 365L

    }
}