package com.github.oslokommune.oslonokkelen.push

/**
 * @param id The permission id is generated on the client side. It has to be unique per client.
 *  We recommend using a reservation number or any other identifier that already exists in your system.
 * @param permissions List of permissions all recipients will be granted access to in Oslonøkkelen.
 *  Each permission contains a time interval and asset identifiers. The asset ids determine what the recipients
 *  will be granted access to.
 * @param recipients List of the recipients identified by phone number.
 * @param attachments Optional information.
 */
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