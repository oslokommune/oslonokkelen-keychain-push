package com.github.oslokommune.oslonokkelen.push

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PhoneNumberTest {

    @Test
    fun `Invalid country code`() {
        val ex = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            PhoneNumber(
                countryCode = "xx",
                phoneNumber = "12312123"
            )
        }

        Assertions.assertThat(ex).hasMessage("Invalid country code")
    }

    @Test
    fun `Invalid country code 2`() {
        val ex = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            PhoneNumber(
                countryCode = "000",
                phoneNumber = "12312123"
            )
        }

        Assertions.assertThat(ex).hasMessage("Invalid country code")
    }

    @Test
    fun `Invalid phone number`() {
        val ex = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            PhoneNumber(
                countryCode = "47",
                phoneNumber = "123"
            )
        }

        Assertions.assertThat(ex).hasMessage("Invalid phone number")
    }

    @Test
    fun `Invalid phone number 2`() {
        val ex = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            PhoneNumber(
                countryCode = "47",
                phoneNumber = "xxxx"
            )
        }

        Assertions.assertThat(ex).hasMessage("Invalid phone number")
    }

    @Test
    fun `Valid phone number`() {
        val number = PhoneNumber(
            countryCode = "47",
            phoneNumber = "12312123"
        )

        assertNotNull(number)
    }

}