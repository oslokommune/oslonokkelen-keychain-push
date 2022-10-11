package com.github.oslokommune.oslonokkelen.push

import java.net.URI

/**
 * Attachments are _optional_ elements that can be added to a push request.
 */
sealed class Attachment {

    data class Link(
        val link: URI,
        val title: String
    ) : Attachment()

    data class AdditionalInformation(
        val content: String,
        val type: Type
    ) : Attachment() {

        enum class Type {
            PLAIN_TEXT, MARKDOWN
        }

    }

}
