package com.github.oslokommune.oslonokkelen.push

import kotlin.time.Instant

/**
 * @param version This version will be incremented for every change to the permission.
 * @param pendingRecipients Recipients who have not used the code they receive on SMS in Oslonøkkelen.
 * @param confirmedRecipients Recipients who have used the code they receive on SMS in the app.
 * @param link Optional link
 * @param information Optional information
 */
data class PermissionState(
    val version: Int,
    val pendingRecipients: List<PendingRecipient>,
    val confirmedRecipients: List<ConfirmedRecipient>,
    val link: Link?,
    val information: Information?
) {

    /**
     * @param phoneNumber Phone number the key code was sent to
     * @param canShare Whether the user can share this permission in the app
     * @param keyCode The key code to be used in the app that was sent to the phone number
     */
    data class PendingRecipient(
        val phoneNumber: PhoneNumber,
        val pushedAt: Instant,
        val canShare: Boolean,
        val keyCode: String
    )

    /**
     * @param phoneNumber Phone number the key code was sent to
     * @param canShare Whether the user can share this permission in the app
     * @param keyCode The key code that was used in the app by the user, if available
     * @param fullName Full name of the user that used the key code
     */
    data class ConfirmedRecipient(
        val phoneNumber: PhoneNumber,
        val usageCounter: Int,
        val pushedAt: Instant,
        val confirmedAt: Instant,
        val canShare: Boolean,
        val keyCode: String?,
        val fullName: String
    )

}