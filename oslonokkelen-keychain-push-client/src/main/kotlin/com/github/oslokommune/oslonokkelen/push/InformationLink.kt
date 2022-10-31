package com.github.oslokommune.oslonokkelen.push

import java.net.URI

/**
 * Each permission can contain a link. This is an optional element, but we strongly
 * suggest that you add a link and point it to a page where the user can see/update/cancel
 * the permission.
 */
data class InformationLink(
    val link: URI,
    val title: String
) {
    init {
        if (title.length > PushRequest.TITLE_MAX_LENGTH) {
            throw IllegalArgumentException("Link title can't be longer then ${PushRequest.TITLE_MAX_LENGTH} characters")
        }
    }
}