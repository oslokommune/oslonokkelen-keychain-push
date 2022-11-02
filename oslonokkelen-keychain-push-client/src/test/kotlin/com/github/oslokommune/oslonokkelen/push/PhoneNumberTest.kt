package com.github.oslokommune.oslonokkelen.push

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class PhoneNumberTest {

    @Test
    fun `Invalid country code`() {
        val ex = assertThrows<IllegalArgumentException> {
            PhoneNumber(
                countryCode = "xx",
                phoneNumber = "12312123"
            )
        }

        Assertions.assertThat(ex).hasMessage("Invalid country code")
    }

    @Test
    fun `Invalid country code 2`() {
        val ex = assertThrows<IllegalArgumentException> {
            PhoneNumber(
                countryCode = "11111",
                phoneNumber = "12312123"
            )
        }

        Assertions.assertThat(ex).hasMessage("Invalid country code")
    }

    @Test
    fun `Invalid country code 3`() {
        val ex = assertThrows<IllegalArgumentException> {
            PhoneNumber(
                countryCode = "0047",
                phoneNumber = "12312123"
            )
        }

        Assertions.assertThat(ex).hasMessage("Don't prefix the country code with zeros")
    }

    @Test
    fun `Invalid phone number`() {
        val ex = assertThrows<IllegalArgumentException> {
            PhoneNumber(
                countryCode = "47",
                phoneNumber = "123"
            )
        }

        Assertions.assertThat(ex).hasMessage("Invalid phone number")
    }

    @Test
    fun `Invalid phone number 2`() {
        val ex = assertThrows<IllegalArgumentException> {
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