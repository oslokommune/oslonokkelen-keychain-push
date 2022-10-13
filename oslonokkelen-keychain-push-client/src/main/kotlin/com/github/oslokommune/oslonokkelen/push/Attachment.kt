package com.github.oslokommune.oslonokkelen.push

import java.net.URI

/**
 * Attachments are _optional_ elements that can be added to a push request.
 */
sealed class Attachment {

    data class Link(
        val link: URI,
        val title: String
    ) : Attachment() {
        init {
            if (title.length > PushRequest.TITLE_MAX_LENGTH) {
                throw IllegalArgumentException("Link title can't be longer then ${PushRequest.TITLE_MAX_LENGTH} characters")
            }
        }
    }

    data class AdditionalInformation(
        val content: String,
        val type: Type
    ) : Attachment() {

        init {
            if (content.length > PushRequest.INFORMATION_MAX_LENGTH) {
                throw IllegalArgumentException("Information can't be longer then ${PushRequest.INFORMATION_MAX_LENGTH} characters")
            }
        }

        enum class Type {
            PLAIN_TEXT, MARKDOWN
        }

    }

}
