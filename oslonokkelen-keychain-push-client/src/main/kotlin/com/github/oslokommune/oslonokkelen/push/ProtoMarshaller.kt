package com.github.oslokommune.oslonokkelen.push

import com.github.oslokommune.oslonokkelen.push.proto.KeychainPushApiV2
import java.net.URI
import java.time.Instant

object ProtoMarshaller {

    fun toProtobuf(request: PermissionList): KeychainPushApiV2.PushRequest {
        val builder = KeychainPushApiV2.PushRequest.newBuilder()
        builder.title = request.title
        builder.id = request.id.id

        for (recipient in request.recipients) {
            builder.addRecipients(
                KeychainPushApiV2.PushRequest.Recipient.newBuilder()
                    .setPhoneNumber(toProtobuf(recipient.phoneNumber))
                    .setCanShare(recipient.canShare)
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

        if (request.link != null) {
            builder.link = toProtobuf(request.link)
        }
        if (request.information != null) {
            builder.information = toProtobuf(request.information)
        }

        return builder.build()
    }

    private fun toProtobuf(link: Link): KeychainPushApiV2.Link {
        return KeychainPushApiV2.Link.newBuilder()
            .setTitle(link.title)
            .setUri(link.uri.toString())
            .build()
    }

    private fun toProtobuf(information: Information): KeychainPushApiV2.Information {
        return KeychainPushApiV2.Information.newBuilder()
            .setContent(information.content)
            .build()
    }

    fun fromProtobuf(protobuf: KeychainPushApiV2.PushRequest): PermissionList {
        return PermissionList.build(protobuf.id, protobuf.title) {
            for (recipient in protobuf.recipientsList) {
                when (recipient.contactInfoCase) {
                    KeychainPushApiV2.PushRequest.Recipient.ContactInfoCase.PHONENUMBER -> {
                        addRecipientByPhoneNumber(recipient.phoneNumber.cc, recipient.phoneNumber.number, recipient.canShare)
                    }

                    KeychainPushApiV2.PushRequest.Recipient.ContactInfoCase.CONTACTINFO_NOT_SET, null -> {
                        throw IllegalStateException("Unsupported recipient type ${recipient.contactInfoCase}")
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

            if (protobuf.hasInformation()) {
                additionalInformation(
                    Information(
                        content = protobuf.information.content
                    )
                )
            }
            if (protobuf.hasLink()) {
                externalLink(
                    Link(
                        title = protobuf.link.title,
                        uri = URI.create(protobuf.link.uri)
                    )
                )
            }
        }
    }


    fun fromProtobuf(message: KeychainPushApiV2.StateResponse): PermissionState {
        return PermissionState(
            version = message.version,
            pendingRecipients = message.pendingRecipientsList.map { pending ->
                PermissionState.PendingRecipient(
                    phoneNumber = PhoneNumber(
                        countryCode = pending.phoneNumber.cc,
                        phoneNumber = pending.phoneNumber.number
                    ),
                    pushedAt = Instant.ofEpochSecond(pending.pushedAtEpochSeconds),
                    canShare = pending.canShare
                )
            },
            confirmedRecipients = message.confirmedRecipientsList.map { confirmed ->
                PermissionState.ConfirmedRecipient(
                    confirmedAt = Instant.ofEpochSecond(confirmed.confirmedAtEpochSeconds),
                    usageCounter = confirmed.usageCounter,
                    phoneNumber = PhoneNumber(
                        countryCode = confirmed.phoneNumber.cc,
                        phoneNumber = confirmed.phoneNumber.number
                    ),
                    pushedAt = Instant.ofEpochSecond(confirmed.pushedAtEpochSeconds),
                    canShare = confirmed.canShare
                )
            },
            information = if (message.hasAdditionalInformation()) {
                Information(
                    content = message.additionalInformation.content
                )
            } else {
                null
            },
            link = if (message.hasInformationLink()) {
                Link(
                    title = message.informationLink.title,
                    uri = URI.create(message.informationLink.uri)
                )
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
                    .setPushedAtEpochSeconds(pending.pushedAt.epochSecond)
                    .setCanShare(pending.canShare)
                    .build()
            })
            .addAllConfirmedRecipients(state.confirmedRecipients.map { confirmed ->
                KeychainPushApiV2.StateResponse.ConfirmedRecipient.newBuilder()
                    .setConfirmedAtEpochSeconds(confirmed.confirmedAt.epochSecond)
                    .setPushedAtEpochSeconds(confirmed.pushedAt.epochSecond)
                    .setPhoneNumber(toProtobuf(confirmed.phoneNumber))
                    .setUsageCounter(confirmed.usageCounter)
                    .setCanShare(confirmed.canShare)
                    .build()
            })
            .setVersion(state.version)

        if (state.link != null) {
            builder.informationLink = toProtobuf(state.link)
        }
        if (state.information != null) {
            builder.additionalInformation = toProtobuf(state.information)
        }

        return builder.build()
    }

    private fun toProtobuf(phoneNumber: PhoneNumber): KeychainPushApiV2.PhoneNumber? =
        KeychainPushApiV2.PhoneNumber.newBuilder()
            .setCc(phoneNumber.countryCode)
            .setNumber(phoneNumber.phoneNumber)
            .build()

    fun toProtobuf(index: PermissionsIndex): KeychainPushApiV2.IndexResponse {
        return KeychainPushApiV2.IndexResponse.newBuilder()
            .addAllEntries(index.entries.map { e ->
                KeychainPushApiV2.IndexResponse.Entry.newBuilder()
                    .setPermissionId(e.id.id)
                    .setVersion(e.version)
                    .build()
            })
            .build()
    }

    fun fromProtobuf(message: KeychainPushApiV2.IndexResponse): PermissionsIndex {
        return PermissionsIndex(
            entries = message.entriesList.map { e ->
                PermissionsIndex.Entry(
                    id = PermissionListId(e.permissionId),
                    version = e.version
                )
            }
        )
    }

}