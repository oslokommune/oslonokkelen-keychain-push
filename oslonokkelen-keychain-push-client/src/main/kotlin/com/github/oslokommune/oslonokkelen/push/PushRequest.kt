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


    companion object {
        fun build(id: String, block: Builder.() -> Unit) : PushRequest {
            val builder = Builder()
            block(builder)

            return PushRequest(
                id = PermissionListId(id),
                permissions = builder.permissions,
                recipients = builder.recipients,
                attachments = builder.attachments
            )
        }
    }


    class Builder {
        val permissions = mutableListOf<Permission>()
        val recipients = mutableListOf<Recipient>()
        val attachments = mutableListOf<Attachment>()

        fun addRecipientByPhoneNumber(countryCode: String, number: String) {
            recipients.add(Recipient(PhoneNumber(countryCode, number)))
        }

        fun addPermission(interval: TimeInterval, assetIds: List<String>) {
            permissions.add(Permission(interval, assetIds.map(::AssetId)))
        }

    }

}