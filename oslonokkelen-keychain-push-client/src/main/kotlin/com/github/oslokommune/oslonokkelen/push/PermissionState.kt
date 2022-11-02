package com.github.oslokommune.oslonokkelen.push

import java.time.Instant

/**
 * @param version This version will be incremented for every change to the permission.
 * @param pendingRecipients Recipients we don't have a profile for. These people will have to confirm their phone number in the app.
 * @param confirmedRecipients Recipients we have found a profile for.
 * @param link Optional link
 * @param additionalInformation Optional information
 */
data class PermissionState(
    val version: Int,
    val pendingRecipients: List<PendingRecipient>,
    val confirmedRecipients: List<ConfirmedRecipient>,
    val link: Link?,
    val additionalInformation: AdditionalInformation?
) {

    data class PendingRecipient(
        val phoneNumber: PhoneNumber
    )

    data class ConfirmedRecipient(
        val phoneNumber: PhoneNumber,
        val usageCounter: Int,
        val confirmedAt: Instant
    )

}