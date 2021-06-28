package com.github.oslokommune.oslonokkelen.kpc.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

internal class PeriodTest {

    @Test
    fun `Can't create period mixing from and until`() {
        val a = LocalDateTime.now()
        val b = a.plusHours(1)

        val ex = assertThrows<IllegalArgumentException> {
            Period(b, a)
        }

        assertThat(ex).hasMessage("Invalid period (from >= until)")
    }

    @Test
    fun `Can create valid period`() {
        val a = LocalDateTime.now()
        val b = a.plusHours(1)
        val p = Period(a, b)

        assertNotNull(p)
    }

    @Test
    fun `Cant grant access too far into the future - We might lift this restriction in the future`() {
        val a = LocalDateTime.now()
        val b = a.plusDays(Period.MAX_DAYS + 1)

        val ex = assertThrows<IllegalArgumentException> {
            Period(a, b)
        }

        assertThat(ex).hasMessage("Can't grant access for more then 365 days")
    }

}