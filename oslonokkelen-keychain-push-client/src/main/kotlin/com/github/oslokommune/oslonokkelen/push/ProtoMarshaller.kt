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
                    .setPhoneNumber(toProtobuf(recipient.phoneNumber))
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

                is Attachment.AdditionalInformation -> {
                    builder.addAttachments(
                        KeychainPushApiV2.PushRequest.Attachment.newBuilder()
                            .setAdditionalInformation(
                                KeychainPushApiV2.AdditionalInformation.newBuilder()
                                    .setContent(attachment.content)
                                    .setType(
                                        when (attachment.type) {
                                            Attachment.AdditionalInformation.Type.PLAIN_TEXT -> KeychainPushApiV2.AdditionalInformation.Type.PLAIN_TEXT
                                            Attachment.AdditionalInformation.Type.MARKDOWN -> KeychainPushApiV2.AdditionalInformation.Type.MARKDOWN
                                        }
                                    )
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

                    KeychainPushApiV2.PushRequest.Attachment.TypeCase.ADDITIONALINFORMATION -> {
                        additionalInformation(
                            content = attachment.additionalInformation.content,
                            type = when (attachment.additionalInformation.type) {
                                KeychainPushApiV2.AdditionalInformation.Type.PLAIN_TEXT -> {
                                    Attachment.AdditionalInformation.Type.PLAIN_TEXT
                                }

                                KeychainPushApiV2.AdditionalInformation.Type.MARKDOWN -> {
                                    Attachment.AdditionalInformation.Type.MARKDOWN
                                }

                                KeychainPushApiV2.AdditionalInformation.Type.UNRECOGNIZED, null -> {
                                    log.warn(
                                        "Interpreting unsupported content type {} as plain text",
                                        attachment.additionalInformation.type
                                    )
                                    Attachment.AdditionalInformation.Type.PLAIN_TEXT
                                }
                            }
                        )
                    }

                    KeychainPushApiV2.PushRequest.Attachment.TypeCase.TYPE_NOT_SET, null -> {
                        log.warn("Skipping unknown attachment: {}", attachment.typeCase)
                    }
                }
            }
        }
    }

    fun fromProtobuf(message: KeychainPushApiV2.StateResponse): PermissionState {
        return PermissionState(
            pendingRecipients = message.pendingRecipientsList.map { pending ->
                PermissionState.PendingRecipient(
                    phoneNumber = PhoneNumber(
                        countryCode = pending.phoneNumber.countryCode,
                        phoneNumber = pending.phoneNumber.number
                    )
                )
            },
            confirmedRecipients = message.confirmedRecipientsList.map { confirmed ->
                PermissionState.ConfirmedRecipient(
                    phoneNumber = PhoneNumber(
                        countryCode = confirmed.phoneNumber.countryCode,
                        phoneNumber = confirmed.phoneNumber.number
                    ),
                    usageCounter = confirmed.usageCounter
                )
            }
        )
    }

    fun toProtobuf(state: PermissionState): KeychainPushApiV2.StateResponse {
        return KeychainPushApiV2.StateResponse.newBuilder()
            .addAllPendingRecipients(state.pendingRecipients.map { pending ->
                KeychainPushApiV2.StateResponse.PendingRecipient.newBuilder()
                    .setPhoneNumber(toProtobuf(pending.phoneNumber))
                    .build()
            })
            .addAllConfirmedRecipients(state.confirmedRecipients.map { confirmed ->
                KeychainPushApiV2.StateResponse.ConfirmedRecipient.newBuilder()
                    .setPhoneNumber(toProtobuf(confirmed.phoneNumber))
                    .setUsageCounter(confirmed.usageCounter)
                    .build()
            })
            .build()
    }

    private fun toProtobuf(phoneNumber: PhoneNumber): KeychainPushApiV2.PhoneNumber? =
        KeychainPushApiV2.PhoneNumber.newBuilder()
            .setCountryCode(phoneNumber.countryCode)
            .setNumber(phoneNumber.phoneNumber)
            .build()

    private val log: Logger = LoggerFactory.getLogger(ProtoMarshaller::class.java)

}