package com.github.oslokommune.oslonokkelen.push

import com.github.oslokommune.oslonokkelen.push.proto.KeychainPushApiV2
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI
import java.time.Instant

object ProtoMarshaller {

    fun toProtobuf(request: PushRequest): KeychainPushApiV2.PushRequest {
        val builder = KeychainPushApiV2.PushRequest.newBuilder()
        builder.title = request.title
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

        if (request.informationLink != null) {
            builder.informationLink = toProtobuf(request.informationLink)
        }
        if (request.additionalInformation != null) {
            builder.additionalInformation = toProtobuf(request.additionalInformation)
        }

        return builder.build()
    }

    private fun toProtobuf(informationLink: InformationLink): KeychainPushApiV2.Link {
        return KeychainPushApiV2.Link.newBuilder()
            .setTitle(informationLink.title)
            .setUri(informationLink.link.toString())
            .build()
    }

    private fun toProtobuf(additionalInformation: AdditionalInformation): KeychainPushApiV2.AdditionalInformation {
        return KeychainPushApiV2.AdditionalInformation.newBuilder()
            .setContent(additionalInformation.content)
            .setType(
                when (additionalInformation.type) {
                    AdditionalInformation.Type.PLAIN_TEXT -> KeychainPushApiV2.AdditionalInformation.Type.PLAIN_TEXT
                    AdditionalInformation.Type.MARKDOWN -> KeychainPushApiV2.AdditionalInformation.Type.MARKDOWN
                }
            )
            .build()
    }

    fun fromProtobuf(protobuf: KeychainPushApiV2.PushRequest): PushRequest {
        return PushRequest.build(protobuf.id, protobuf.title) {
            for (recipient in protobuf.recipientsList) {
                when (recipient.recipientCase) {
                    KeychainPushApiV2.PushRequest.Recipient.RecipientCase.PHONENUMBER -> {
                        addRecipientByPhoneNumber(recipient.phoneNumber.cc, recipient.phoneNumber.number)
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

            if (protobuf.hasAdditionalInformation()) {
                additionalInformation(fromProtobuf(protobuf.additionalInformation))
            }
            if (protobuf.hasInformationLink()) {
                externalLink(fromProtobuf(protobuf.informationLink))
            }
        }
    }

    private fun fromProtobuf(informationLink: KeychainPushApiV2.Link): InformationLink {
        return InformationLink(
            title = informationLink.title,
            link = URI.create(informationLink.uri)
        )
    }

    private fun fromProtobuf(additionalInformation: KeychainPushApiV2.AdditionalInformation): AdditionalInformation {
        return AdditionalInformation(
            content = additionalInformation.content,
            type = when (additionalInformation.type) {
                KeychainPushApiV2.AdditionalInformation.Type.PLAIN_TEXT -> {
                    AdditionalInformation.Type.PLAIN_TEXT
                }

                KeychainPushApiV2.AdditionalInformation.Type.MARKDOWN -> {
                    AdditionalInformation.Type.MARKDOWN
                }

                KeychainPushApiV2.AdditionalInformation.Type.UNRECOGNIZED, null -> {
                    log.warn(
                        "Interpreting unsupported content type {} as plain text",
                        additionalInformation.type
                    )
                    AdditionalInformation.Type.PLAIN_TEXT
                }
            }
        )
    }


    fun fromProtobuf(message: KeychainPushApiV2.StateResponse): PermissionState {
        return PermissionState(
            version = message.version,
            pendingRecipients = message.pendingRecipientsList.map { pending ->
                PermissionState.PendingRecipient(
                    phoneNumber = PhoneNumber(
                        countryCode = pending.phoneNumber.cc,
                        phoneNumber = pending.phoneNumber.number
                    )
                )
            },
            confirmedRecipients = message.confirmedRecipientsList.map { confirmed ->
                PermissionState.ConfirmedRecipient(
                    confirmedAt = Instant.ofEpochSecond(confirmed.confirmedAtEpochSeconds),
                    usageCounter = confirmed.usageCounter,
                    phoneNumber = PhoneNumber(
                        countryCode = confirmed.phoneNumber.cc,
                        phoneNumber = confirmed.phoneNumber.number
                    )
                )
            },
            additionalInformation = if (message.hasAdditionalInformation()) {
                fromProtobuf(message.additionalInformation)
            } else {
                null
            },
            informationLink = if (message.hasInformationLink()) {
                fromProtobuf(message.informationLink)
            } else {
                null
            }
        )
    }

    fun toProtobuf(state: PermissionState): KeychainPushApiV2.StateResponse {
        val builder = KeychainPushApiV2.StateResponse.newBuilder()
            .addAllPendingRecipients(state.pendingRecipients.map { pending ->
                KeychainPushApiV2.StateResponse.PendingRecipient.newBuilder()
                    .setPhoneNumber(toProtobuf(pending.phoneNumber))
                    .build()
            })
            .addAllConfirmedRecipients(state.confirmedRecipients.map { confirmed ->
                KeychainPushApiV2.StateResponse.ConfirmedRecipient.newBuilder()
                    .setConfirmedAtEpochSeconds(confirmed.confirmedAt.epochSecond)
                    .setPhoneNumber(toProtobuf(confirmed.phoneNumber))
                    .setUsageCounter(confirmed.usageCounter)
                    .build()
            })
            .setVersion(state.version)

        if (state.informationLink != null) {
            builder.informationLink = toProtobuf(state.informationLink)
        }
        if (state.additionalInformation != null) {
            builder.additionalInformation = toProtobuf(state.additionalInformation)
        }

        return builder.build()
    }

    private fun toProtobuf(phoneNumber: PhoneNumber): KeychainPushApiV2.PhoneNumber? =
        KeychainPushApiV2.PhoneNumber.newBuilder()
            .setCc(phoneNumber.countryCode)
            .setNumber(phoneNumber.phoneNumber)
            .build()

    private val log: Logger = LoggerFactory.getLogger(ProtoMarshaller::class.java)

}