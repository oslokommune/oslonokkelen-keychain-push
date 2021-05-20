package com.github.oslokommune.oslonokkelen.kpc.serialization

import com.github.oslokommune.oslonokkelen.keychainpush.proto.KeychainPushApi
import com.github.oslokommune.oslonokkelen.keychainpush.proto.KeychainPushApi.PushKeychainRequest.Recipient.LookupKeyCase
import com.github.oslokommune.oslonokkelen.keychainpush.proto.KeychainPushApi.PushKeychainRequest.TextContentType.MARKDOWN
import com.github.oslokommune.oslonokkelen.keychainpush.proto.KeychainPushApi.PushKeychainRequest.TextContentType.PLAIN_TEXT
import com.github.oslokommune.oslonokkelen.kpc.model.InformationForUser
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest
import com.github.oslokommune.oslonokkelen.kpc.model.Period
import com.github.oslokommune.oslonokkelen.kpc.model.ProfileLookupKey
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

object KeychainPushSerializer {

    fun toProtobuf(request: KeychainPushRequest): KeychainPushApi.PushKeychainRequest {
        val informationForUser = KeychainPushApi.PushKeychainRequest.InformationForUser.newBuilder()
        informationForUser.title = request.informationForUser.title

        if (request.informationForUser.moreInfoUri != null) {
            informationForUser.moreInfoLink = request.informationForUser.moreInfoUri.toString()
        }
        if (request.informationForUser.information != null) {
            informationForUser.message = request.informationForUser.information
            informationForUser.messageContentType = if (request.informationForUser.informationIsMarkdown) {
                MARKDOWN
            } else {
                PLAIN_TEXT
            }
        }

        return KeychainPushApi.PushKeychainRequest.newBuilder()
                .addAllRecipients(request.recipients.map { key: ProfileLookupKey ->
                    when (key) {
                        is ProfileLookupKey.PhoneNumber -> {
                            KeychainPushApi.PushKeychainRequest.Recipient.newBuilder()
                                    .setPhoneNumber(KeychainPushApi.PushKeychainRequest.PhoneNumber.newBuilder()
                                            .setCountryCode(key.countryCode)
                                            .setPhoneNumber(key.number)
                                            .build())
                                    .build()
                        }
                    }
                })
                .addAllPeriods(request.periods.map { p ->
                    KeychainPushApi.PushKeychainRequest.Period.newBuilder()
                            .setFrom(p.from.format(ISO_LOCAL_DATE_TIME))
                            .setUntil(p.until.format(ISO_LOCAL_DATE_TIME))
                            .build()
                })
                .setInformationForUser(informationForUser)
                .build()
    }

    fun fromProtobuf(request: KeychainPushApi.PushKeychainRequest): KeychainPushRequest {
        return KeychainPushRequest(
                recipients = request.recipientsList.map { key ->
                    when (key.lookupKeyCase) {
                        LookupKeyCase.PHONENUMBER -> {
                            ProfileLookupKey.PhoneNumber(
                                    countryCode = key.phoneNumber.countryCode,
                                    number = key.phoneNumber.phoneNumber
                            )
                        }
                        LookupKeyCase.LOOKUPKEY_NOT_SET, null -> {
                            throw IllegalArgumentException("Unsupported profile lookup key: ${key.lookupKeyCase}")
                        }
                    }
                },
                periods = request.periodsList.map { p ->
                    Period(
                            from = LocalDateTime.parse(p.from),
                            until = LocalDateTime.parse(p.until)
                    )
                },
                informationForUser = InformationForUser(
                        information = if (request.informationForUser?.message != "") {
                            request.informationForUser.message
                        } else {
                            null
                        },
                        informationIsMarkdown = request.informationForUser?.messageContentType == MARKDOWN,
                        moreInfoUri = if (request.informationForUser?.moreInfoLink != "") {
                            URI.create(request.informationForUser.moreInfoLink)
                        } else {
                            null
                        },
                        title = request.informationForUser.title
                )
        )
    }

}