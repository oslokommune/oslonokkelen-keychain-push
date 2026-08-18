package com.github.oslokommune.oslonokkelen.push

import java.time.Instant

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

    data class PendingRecipient(
        val phoneNumber: PhoneNumber,
        val pushedAt: Instant,
        val canShare: Boolean
    )

    data class ConfirmedRecipient(
        val phoneNumber: PhoneNumber,
        val usageCounter: Int,
        val pushedAt: Instant,
        val confirmedAt: Instant,
        val canShare: Boolean
    )

}