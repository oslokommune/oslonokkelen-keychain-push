package com.github.oslokommune.oslonokkelen.kpc.model

import java.lang.IllegalArgumentException
import java.net.URI

/**
 * Try to keep title and information short and relevant.
 */
data class InformationForUser(
        val title: String,
        val moreInfoUri: URI? = null,
        val information: String? = null,
        val informationIsMarkdown: Boolean = false
) {
    init {
        if (title.isBlank()) {
            throw IllegalArgumentException("Title can't be blank")
        }
        if (title.length > TITLE_MAX_LENGTH) {
            throw IllegalArgumentException("Title can't be longer then $TITLE_MAX_LENGTH characters")
        }
        if (information != null && information.length > INFORMATION_MAX_LENGTH) {
            throw IllegalArgumentException("Information can't be longer then $INFORMATION_MAX_LENGTH characters")
        }
    }

    companion object {
        const val TITLE_MAX_LENGTH = 100
        const val INFORMATION_MAX_LENGTH = 2000
    }
}
