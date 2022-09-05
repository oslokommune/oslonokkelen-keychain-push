package com.github.oslokommune.oslonokkelen.push.permission

/**
 * Oslonøkkelen identifies recipients by their phone number.
 * Everyone who wants to be able to receive "push permissions" in Oslonøkkelen
 * will have to verify their phone number in the app.
 */
data class Recipient(
    val phoneNumber: PhoneNumber
)