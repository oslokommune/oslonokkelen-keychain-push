package com.github.oslokommune.oslonokkelen.push

data class PermissionState(
    val pendingRecipients: List<PendingRecipient>,
    val confirmedRecipients: List<ConfirmedRecipient>,
    val attachments: List<Attachment>,
) {


    data class PendingRecipient(
        val phoneNumber: PhoneNumber
    )

    data class ConfirmedRecipient(
        val phoneNumber: PhoneNumber,
        val usageCounter: Int
    )

}