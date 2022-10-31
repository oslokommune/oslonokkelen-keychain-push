package com.github.oslokommune.oslonokkelen.push

import java.net.URI

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