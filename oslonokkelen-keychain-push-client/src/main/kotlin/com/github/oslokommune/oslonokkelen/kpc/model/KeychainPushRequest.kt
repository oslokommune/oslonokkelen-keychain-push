package com.github.oslokommune.oslonokkelen.kpc.model

import java.net.URI
import java.time.LocalDateTime

/**
 * @param recipients How can Oslonøkkelen identify the recipients
 * @param periods When the recipients should be allowed access
 * @param informationForUser "Nice to know" information to be presented in the app
 */
data class KeychainPushRequest(
        val recipients: List<ProfileLookupKey>,
        val periods: List<Period>,
        val informationForUser: InformationForUser
) {

    init {
        if (recipients.isEmpty()) {
            throw IllegalArgumentException("Must contain at least one recipient")
        }
        if (periods.isEmpty()) {
            throw IllegalArgumentException("Must contain at least one period")
        }
    }


    companion object {
        fun build(title: String, block: Builder.() -> Unit): KeychainPushRequest {
            val builder = Builder(title)
            block(builder)

            return KeychainPushRequest(
                    recipients = builder.recipients,
                    periods = builder.periods,
                    informationForUser = InformationForUser(
                            moreInfoUri = builder.moreInfoUri,
                            information = builder.information,
                            informationIsMarkdown = builder.markdown,
                            title = builder.title
                    )
            )
        }
    }


    class Builder(internal val title: String) {

        internal val recipients = mutableListOf<ProfileLookupKey>()
        internal val periods = mutableListOf<Period>()

        internal var moreInfoUri: URI? = null
        internal var information: String? = null
        internal var markdown = false

        fun recipientByPhoneNumber(countryCode: String, number: String) {
            recipients.add(ProfileLookupKey.PhoneNumber(countryCode, number))
        }

        fun accessBetween(from: String, until: String) {
            accessBetween(LocalDateTime.parse(from), LocalDateTime.parse(until))
        }

        fun accessBetween(from: LocalDateTime, until: LocalDateTime) {
            periods.add(Period(from, until))
        }

        fun moreInformationLink(uri: String) {
            moreInfoUri = URI.create(uri)
        }

        fun information(information: String, isMarkdown: Boolean = false) {
            this.information = information
            this.markdown = isMarkdown
        }

    }

}