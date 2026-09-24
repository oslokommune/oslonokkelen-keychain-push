package com.github.oslokommune.oslonokkelen.push

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional

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

        /**
         * Wire format for dates, e.g. 2022-01-31
         */
        internal val dateFormat: DateTimeFormat<LocalDate> = LocalDate.Formats.ISO

        /**
         * Wire format for times, e.g. 13:15. Seconds are only included when non-zero, and fractions of a second are never included.
         */
        internal val timeFormat: DateTimeFormat<LocalTime> = LocalTime.Format {
            hour()
            char(':')
            minute()
            optional {
                char(':')
                second()
            }
        }

        /**
         * Lenient version of [timeFormat] also accepting fractions of a second, e.g. 13:15:30.5
         */
        private val lenientTimeFormat: DateTimeFormat<LocalTime> = LocalTime.Format {
            hour()
            char(':')
            minute()
            optional {
                char(':')
                second()
                optional {
                    char('.')
                    secondFraction(1, 9)
                }
            }
        }

        /**
         * Parses a time, discarding anything more precise than seconds.
         */
        internal fun parseTime(input: String): LocalTime {
            val time = lenientTimeFormat.parse(input)
            return LocalTime(time.hour, time.minute, time.second)
        }

        fun parse(fromDate: String, fromTime: String, endDate: String, endTime: String) : TimeInterval {
            return TimeInterval(
                start = dateFormat.parse(fromDate).atTime(parseTime(fromTime)),
                end = dateFormat.parse(endDate).atTime(parseTime(endTime))
            )
        }
    }


}
