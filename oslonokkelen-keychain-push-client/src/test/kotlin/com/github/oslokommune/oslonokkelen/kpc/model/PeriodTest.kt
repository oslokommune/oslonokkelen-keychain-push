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

}