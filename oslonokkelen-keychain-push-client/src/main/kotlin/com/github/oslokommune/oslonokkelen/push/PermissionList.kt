package com.github.oslokommune.oslonokkelen.push

import java.net.URI

/**
 * @param id The permission id is generated on the client side. It has to be unique per client.
 *  We recommend using a reservation number or any other identifier that already exists in your system.
 * @param title Short and human-readable title. Will show up in the app.
 * @param permissions List of permissions all recipients will be granted access to in Oslonøkkelen.
 *  Each permission contains a time interval and asset identifiers. The asset ids determine what the recipients
 *  will be granted access to.
 * @param recipients List of the recipients identified by phone number.
 * @param link Optional link
 * @param information Optional information
 */
data class PermissionList(
    val id: PermissionListId,
    val title: String,
    val permissions: List<Permission>,
    val recipients: List<Recipient>,
    val link: Link?,
    val information: Information?
) {
    init {
        if (title.isBlank()) {
            throw IllegalArgumentException("Title can't be blank")
        }
        if (title.length > TITLE_MAX_LENGTH) {
            throw IllegalArgumentException("Title can't be longer then $TITLE_MAX_LENGTH characters")
        }
        if (recipients.isEmpty()) {
            throw IllegalArgumentException("Must have at least one recipient")
        }
        if (permissions.isEmpty()) {
            throw IllegalArgumentException("Must have at least one permission")
        }
    }


    companion object {

        const val TITLE_MAX_LENGTH = 100
        const val INFORMATION_MAX_LENGTH = 2000

        fun build(id: String, title: String, block: Builder.() -> Unit): PermissionList {
            val builder = Builder()
            block(builder)

            return PermissionList(
                id = PermissionListId(id),
                title = title,
                permissions = builder.permissions,
                recipients = builder.recipients,
                link = builder.link,
                information = builder.information
            )
        }
    }


    class Builder {
        val permissions = mutableListOf<Permission>()
        val recipients = mutableListOf<Recipient>()

        var link: Link? = null
        var information: Information? = null


        fun addRecipientByPhoneNumber(countryCode: String, number: String, canShare: Boolean) {
            recipients.add(Recipient(PhoneNumber(countryCode, number),  canShare))
        }

        fun addPermission(interval: TimeInterval, assetIds: List<String>) {
            permissions.add(Permission(interval, assetIds.map(::AssetId)))
        }

        fun externalLink(title: String, uri: URI) {
            link = Link(
                title = title,
                uri = uri
            )
        }

        fun externalLink(link: Link) {
            this.link = link
        }

        fun additionalInformation(content: String) {
            information = Information(
                content = content
            )
        }

        fun additionalInformation(info: Information) {
            information = info
        }

    }

}