package com.github.oslokommune.oslonokkelen.kpc.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class InformationForUserTest {

    @Test
    fun `Blank title is not allowed`() {
        val ex = assertThrows<IllegalArgumentException> {
            InformationForUser(title = " ")
        }

        assertThat(ex).hasMessage("Title can't be blank")
    }

    @Test
    fun `Title can't exceed max length`() {
        val ex = assertThrows<IllegalArgumentException> {
            InformationForUser(title = ".".repeat(InformationForUser.TITLE_MAX_LENGTH + 1))
        }

        assertThat(ex).hasMessage("Title can't be longer then 100 characters")
    }

    @Test
    fun `Information can't exceed max length`() {
        val ex = assertThrows<IllegalArgumentException> {
            InformationForUser(
                    title = "Hei",
                    information = ".".repeat(InformationForUser.INFORMATION_MAX_LENGTH + 1)
            )
        }

        assertThat(ex).hasMessage("Information can't be longer then 2000 characters")
    }

}