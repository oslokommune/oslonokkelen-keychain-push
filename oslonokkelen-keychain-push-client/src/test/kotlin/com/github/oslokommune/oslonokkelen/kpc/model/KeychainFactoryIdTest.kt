package com.github.oslokommune.oslonokkelen.kpc.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class KeychainFactoryIdTest {

    @Test
    fun `Is not allowed to create an empty id`() {
        val ex = assertThrows<IllegalArgumentException> {
            KeychainFactoryId("")
        }

        assertThat(ex).hasMessage("'' is not a valid id")
    }

    @Test
    fun `Is not allowed to create an id that is too long`() {
        val ex = assertThrows<IllegalArgumentException> {
            KeychainFactoryId("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
        }

        assertThat(ex).hasMessage("'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx' is not a valid id")
    }

}