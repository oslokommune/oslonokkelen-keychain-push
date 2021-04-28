package com.github.oslokommune.oslonokkelen.kpc.model

/**
 * @param recipients How can Oslonøkkelen identify the recipients
 * @param periods When the recipients should be allowed access
 * @param informationForUser "Nice to know" information to be presented in the app
 */
data class KeychainPushRequest(
        val recipients: List<ProfileLookupKey>,
        val periods: List<Period>,
        val informationForUser: InformationForUser = InformationForUser()
) {

    init {
        if (recipients.isEmpty()) {
            throw IllegalArgumentException("Must contain at least one recipient")
        }
        if (periods.isEmpty()) {
            throw IllegalArgumentException("Must contain at least one period")
        }
    }

}