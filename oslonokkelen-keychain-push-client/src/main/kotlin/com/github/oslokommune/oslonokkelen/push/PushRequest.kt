package com.github.oslokommune.oslonokkelen.push

import com.github.oslokommune.oslonokkelen.kpc.model.InformationForUser
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
 * @param additionalInformation Optional information
 */
data class PushRequest(
    val id: PermissionListId,
    val title: String,
    val permissions: List<Permission>,
    val recipients: List<Recipient>,
    val link: Link?,
    val additionalInformation: AdditionalInformation?
) {
    init {
        if (title.isBlank()) {
            throw IllegalArgumentException("Title can't be blank")
        }
        if (title.length > InformationForUser.TITLE_MAX_LENGTH) {
            throw IllegalArgumentException("Title can't be longer then ${InformationForUser.TITLE_MAX_LENGTH} characters")
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

        fun build(id: String, title: String, block: Builder.() -> Unit): PushRequest {
            val builder = Builder()
            block(builder)

            return PushRequest(
                id = PermissionListId(id),
                title = title,
                permissions = builder.permissions,
                recipients = builder.recipients,
                link = builder.link,
                additionalInformation = builder.additionalInformation
            )
        }
    }


    class Builder {
        val permissions = mutableListOf<Permission>()
        val recipients = mutableListOf<Recipient>()

        var link: Link? = null
        var additionalInformation: AdditionalInformation? = null


        fun addRecipientByPhoneNumber(countryCode: String, number: String) {
            recipients.add(Recipient(PhoneNumber(countryCode, number)))
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

        fun additionalPlainTextInformation(content: String) {
            additionalInformation = AdditionalInformation(
                type = AdditionalInformation.Type.PLAIN_TEXT,
                content = content
            )
        }

        fun additionalInformation(content: String, type: AdditionalInformation.Type) {
            additionalInformation = AdditionalInformation(
                content = content,
                type = type
            )
        }

        fun additionalInformation(info: AdditionalInformation) {
            additionalInformation = info
        }

    }

}