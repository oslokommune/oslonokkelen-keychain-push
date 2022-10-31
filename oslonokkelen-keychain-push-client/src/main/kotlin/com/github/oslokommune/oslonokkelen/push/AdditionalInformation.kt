package com.github.oslokommune.oslonokkelen.push

data class AdditionalInformation(
    val content: String,
    val type: Type
) {

    init {
        if (content.length > PushRequest.INFORMATION_MAX_LENGTH) {
            throw IllegalArgumentException("Information can't be longer then ${PushRequest.INFORMATION_MAX_LENGTH} characters")
        }
    }

    enum class Type {
        PLAIN_TEXT, MARKDOWN
    }

}