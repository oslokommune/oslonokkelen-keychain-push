package com.github.oslokommune.oslonokkelen.push

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class TimeInterval(
    val start: LocalDateTime,
    val end: LocalDateTime
) {
    init {
        if (end < start) {
            throw IllegalArgumentException("Invalid interval: $start -> $end")
        }
    }


    companion object {
        fun parse(fromDate: String, fromTime: String, endDate: String, endTime: String) : TimeInterval {
            return TimeInterval(
                start = LocalDate.parse(fromDate).atTime(LocalTime.parse(fromTime)),
                end = LocalDate.parse(endDate).atTime(LocalTime.parse(endTime))
            )
        }
    }


}