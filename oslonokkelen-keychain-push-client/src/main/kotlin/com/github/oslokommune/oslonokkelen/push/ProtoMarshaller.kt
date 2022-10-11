package com.github.oslokommune.oslonokkelen.push

import com.github.oslokommune.oslonokkelen.push.proto.KeychainPushApiV2
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI

object ProtoMarshaller {

    fun toProtobuf(request: PushRequest): KeychainPushApiV2.PushRequest {
        val builder = KeychainPushApiV2.PushRequest.newBuilder()
        builder.id = request.id.id

        for (recipient in request.recipients) {
            builder.addRecipients(
                KeychainPushApiV2.PushRequest.Recipient.newBuilder()
                    .setPhoneNumber(
                        KeychainPushApiV2.PhoneNumber.newBuilder()
                            .setCountryCode(recipient.phoneNumber.countryCode)
                            .setNumber(recipient.phoneNumber.phoneNumber)
                            .build()
                    )
                    .build()
            )
        }

        for (permission in request.permissions) {
            builder.addPermissions(
                KeychainPushApiV2.PushRequest.Permission.newBuilder()
                    .addAllAssetIds(permission.assetIds.map { assetId -> assetId.id })
                    .setTimeInterval(
                        KeychainPushApiV2.LocalDateTimeInterval.newBuilder()
                            .setFrom(
                                KeychainPushApiV2.LocalDateTime.newBuilder()
                                    .setDate(permission.timeInterval.start.toLocalDate().toString())
                                    .setTime(permission.timeInterval.start.toLocalTime().toString())
                                    .build()
                            )
                            .setUntil(
                                KeychainPushApiV2.LocalDateTime.newBuilder()
                                    .setDate(permission.timeInterval.end.toLocalDate().toString())
                                    .setTime(permission.timeInterval.end.toLocalTime().toString())
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
        }

        for (attachment in request.attachments) {
            when (attachment) {
                is Attachment.Link -> {
                    builder.addAttachments(
                        KeychainPushApiV2.PushRequest.Attachment.newBuilder()
                            .setLink(
                                KeychainPushApiV2.Link.newBuilder()
                                    .setTitle(attachment.title)
                                    .setUri(attachment.link.toString())
                                    .build()
                            )
                            .build()
                    )
                }
            }
        }

        return builder.build()
    }

    fun fromProtobuf(protobuf: KeychainPushApiV2.PushRequest): PushRequest {
        return PushRequest.build(protobuf.id) {
            for (recipient in protobuf.recipientsList) {
                when (recipient.recipientCase) {
                    KeychainPushApiV2.PushRequest.Recipient.RecipientCase.PHONENUMBER -> {
                        addRecipientByPhoneNumber(recipient.phoneNumber.countryCode, recipient.phoneNumber.number)
                    }

                    KeychainPushApiV2.PushRequest.Recipient.RecipientCase.RECIPIENT_NOT_SET, null -> {
                        throw IllegalStateException("Unsupported recipient type ${recipient.recipientCase}")
                    }
                }
            }
            for (permission in protobuf.permissionsList) {
                addPermission(
                    interval = TimeInterval.parse(
                        fromDate = permission.timeInterval.from.date,
                        fromTime = permission.timeInterval.from.time,
                        endDate = permission.timeInterval.until.date,
                        endTime = permission.timeInterval.until.time,
                    ),
                    assetIds = permission.assetIdsList
                )
            }
            for (attachment in protobuf.attachmentsList) {
                when (attachment.typeCase) {
                    KeychainPushApiV2.PushRequest.Attachment.TypeCase.LINK -> {
                        externalLink(attachment.link.title, URI.create(attachment.link.uri))
                    }
                    KeychainPushApiV2.PushRequest.Attachment.TypeCase.ADDITIONALINFORMATION -> TODO()
                    KeychainPushApiV2.PushRequest.Attachment.TypeCase.TYPE_NOT_SET, null -> {
                        log.warn("Skipping unknown attachment: {}", attachment.typeCase)
                    }
                }
            }
        }
    }

    private val log: Logger = LoggerFactory.getLogger(ProtoMarshaller::class.java)

}