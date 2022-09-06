package com.github.oslokommune.oslonokkelen.push

import java.time.LocalDateTime

data class TimeInterval(
    val start: LocalDateTime,
    val end: LocalDateTime
) {
    init {
        if (end < start) {
            throw IllegalArgumentException("Invalid interval: $start -> $end")
        }
    }
}