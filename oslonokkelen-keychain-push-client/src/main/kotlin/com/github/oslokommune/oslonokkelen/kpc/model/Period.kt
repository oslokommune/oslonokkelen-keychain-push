package com.github.oslokommune.oslonokkelen.kpc.model

import java.time.LocalDateTime

data class Period(
        val from: LocalDateTime,
        val until: LocalDateTime
) {
    init {
        if (from >= until) {
            throw IllegalArgumentException("Invalid period (from >= until)")
        }
    }

    override fun toString(): String {
        return "$from - $until"
    }
}