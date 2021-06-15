package com.github.oslokommune.oslonokkelen.kpc.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ProfileLookupKeyTest {

    @Test
    fun `xx is not a valid country code`() {
        val ex = assertThrows<IllegalArgumentException> {
            ProfileLookupKey.PhoneNumber("xx", "12312123")
        }

        assertThat(ex).hasMessage("Invalid country code 'xx' (valid example '47')")
    }

    @Test
    fun `123 is not a valid country code`() {
        val ex = assertThrows<IllegalArgumentException> {
            ProfileLookupKey.PhoneNumber("123", "12312123")
        }

        assertThat(ex).hasMessage("Invalid country code '123' (valid example '47')")
    }

    @Test
    fun `1 is not a valid country code`() {
        val ex = assertThrows<IllegalArgumentException> {
            ProfileLookupKey.PhoneNumber("1", "12312123")
        }

        assertThat(ex).hasMessage("Invalid country code '1' (valid example '47')")
    }

    @Test
    fun `Phone numbers with letters are not cool`() {
        val ex = assertThrows<IllegalArgumentException> {
            ProfileLookupKey.PhoneNumber("47", "123kake")
        }

        assertThat(ex).hasMessage("Invalid phone number '123kake'")
    }

    @Test
    fun `We dont accept too short phone numbers`() {
        val ex = assertThrows<IllegalArgumentException> {
            ProfileLookupKey.PhoneNumber("47", "02800")
        }

        assertThat(ex).hasMessage("Invalid phone number '02800'")
    }

    @Test
    fun `We dont accept too long phone numbers`() {
        val ex = assertThrows<IllegalArgumentException> {
            ProfileLookupKey.PhoneNumber("47", "123123123123123123123")
        }

        assertThat(ex).hasMessage("Invalid phone number '123123123123123123123'")
    }

    @Test
    fun `47 is a valid country code`() {
        val key = ProfileLookupKey.PhoneNumber("47", "12312123")

        assertEquals("47", key.countryCode)
        assertEquals("12312123", key.number)
    }

}