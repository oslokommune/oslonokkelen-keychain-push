package com.github.oslokommune.oslonokkelen.push

/**
 * Useful information that will be shown to the user in the app. Keep in mind that the
 * app is not a good place to read long texts so keep it short and to the point!
 */
data class Information(val content: String) {
    init {
        if (content.length > PushRequest.INFORMATION_MAX_LENGTH) {
            throw IllegalArgumentException("Information can't be longer then ${PushRequest.INFORMATION_MAX_LENGTH} characters")
        }
    }
}