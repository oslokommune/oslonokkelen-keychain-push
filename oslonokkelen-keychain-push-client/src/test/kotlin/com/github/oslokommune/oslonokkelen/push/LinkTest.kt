package com.github.oslokommune.oslonokkelen.push

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.URI

internal class LinkTest {

    @Test
    fun `Too long title`() {
        val ex = assertThrows<IllegalArgumentException> {
            Link(
                uri = URI.create("https://vg.no"),
                title = " ".repeat(Link.LINK_TITLE_MAX_LENGTH) + 1
            )
        }

        assertThat(ex).hasMessage("Link title can't be longer then 20 characters (it must be able to fit within a button in the app)")
    }

    @Test
    fun `Valid title`() {
        val link = Link(
            uri = URI.create("https://vg.no"),
            title = " ".repeat(Link.LINK_TITLE_MAX_LENGTH)
        )

        assertNotNull(link)
    }

}