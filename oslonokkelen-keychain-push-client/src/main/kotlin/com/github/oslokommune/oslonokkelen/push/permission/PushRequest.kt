package com.github.oslokommune.oslonokkelen.push.permission

import com.github.oslokommune.oslonokkelen.push.PermissionId

data class PushRequest(
    val permissionID: PermissionId,
    val grants: List<Grant>,
    val recipients: List<Recipient>,
    val attachments: List<Attachment> = emptyList()
) {
    init {
        if (recipients.isEmpty()) {
            throw IllegalArgumentException("Must have at least one recipient")
        }
        if (grants.isEmpty()) {
            throw java.lang.IllegalArgumentException("Must have at least one grant")
        }
    }
}