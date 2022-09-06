package com.github.oslokommune.oslonokkelen.push

data class PushRequest(
    val id: PermissionListId,
    val permissions: List<Permission>,
    val recipients: List<Recipient>,
    val attachments: List<Attachment> = emptyList()
) {
    init {
        if (recipients.isEmpty()) {
            throw IllegalArgumentException("Must have at least one recipient")
        }
        if (permissions.isEmpty()) {
            throw java.lang.IllegalArgumentException("Must have at least one permission")
        }
    }
}