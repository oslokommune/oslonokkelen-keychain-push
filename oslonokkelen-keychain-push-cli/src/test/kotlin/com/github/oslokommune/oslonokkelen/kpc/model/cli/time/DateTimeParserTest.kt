package com.github.oslokommune.oslonokkelen.kpc.model.cli.time

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class DateTimeParserTest {

    @Test
    fun `Can parse local date time`() {
        val parser = DateTimeParser.newParser("2021-11-01T10:00:00")
        val parsed = parser.parse("2021-11-01T10:00:00")

        assertThat(parsed).isEqualTo(LocalDateTime.parse("2021-11-01T10:00:00"))
    }

    @Test
    fun `Understand the concept of 'now'`() {
        val parser = DateTimeParser.newParser("2021-11-01T10:00:00")
        val parsed = parser.parse("now")

        assertThat(parsed).isEqualTo(LocalDateTime.parse("2021-11-01T10:00:00"))
    }

    @Test
    fun `Understand the concept of 'now+2h'`() {
        val parser = DateTimeParser.newParser("2021-11-01T10:00:00")
        val parsed = parser.parse("now+2h")

        assertThat(parsed).isEqualTo(LocalDateTime.parse("2021-11-01T12:00:00"))
    }

    @Test
    fun `Understand the concept of 'now+2d+2h'`() {
        val parser = DateTimeParser.newParser("2021-11-01T10:00:00")
        val parsed = parser.parse("now+2d+2h")

        assertThat(parsed).isEqualTo(LocalDateTime.parse("2021-11-03T12:00:00"))
    }

    @Test
    fun `Understand the concept of 'now+2d+2h-30m'`() {
        val parser = DateTimeParser.newParser("2021-11-01T10:00:00")
        val parsed = parser.parse("now+2d+2h-30m")

        assertThat(parsed).isEqualTo(LocalDateTime.parse("2021-11-03T11:30:00"))
    }


}