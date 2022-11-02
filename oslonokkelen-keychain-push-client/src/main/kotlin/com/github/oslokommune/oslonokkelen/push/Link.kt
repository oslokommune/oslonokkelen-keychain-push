package com.github.oslokommune.oslonokkelen.push

import java.net.URI

/**
 * Each permission can contain a link. This is an optional element, but we strongly
 * suggest that you add a link and point it to a page where the user can see/update/cancel
 * the permission.
 */
data class Link(
    val title: String,
    val uri: URI
) {
    init {
        if (title.length > LINK_TITLE_MAX_LENGTH) {
            throw IllegalArgumentException("Link title can't be longer then $LINK_TITLE_MAX_LENGTH characters (it must be able to fit within a button in the app)")
        }
    }

    companion object {
        const val LINK_TITLE_MAX_LENGTH = 20
    }
}