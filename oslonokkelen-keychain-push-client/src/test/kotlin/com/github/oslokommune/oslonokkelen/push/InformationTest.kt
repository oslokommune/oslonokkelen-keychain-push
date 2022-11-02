package com.github.oslokommune.oslonokkelen.push

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class InformationTest {

    @Test
    fun `Too long`() {
        val ex = assertThrows<IllegalArgumentException> {
            Information(
                content = " ".repeat(PushRequest.INFORMATION_MAX_LENGTH+1)
            )
        }

        assertThat(ex).hasMessage("Information can't be longer then 2000 characters")
    }

}