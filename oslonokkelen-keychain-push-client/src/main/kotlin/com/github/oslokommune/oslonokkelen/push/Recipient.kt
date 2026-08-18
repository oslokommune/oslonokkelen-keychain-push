package com.github.oslokommune.oslonokkelen.push

/**
 * Oslonøkkelen sends a key code to users phone numbers that they have to use in the app.
 */
data class Recipient(
    val phoneNumber: PhoneNumber,
    val canShare: Boolean
)