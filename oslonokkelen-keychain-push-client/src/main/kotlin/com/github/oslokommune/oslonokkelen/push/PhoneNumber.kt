package com.github.oslokommune.oslonokkelen.push


data class PhoneNumber(
    val countryCode: String,
    val phoneNumber: String
) {
    init {
        if (!countryCode.matches(countryCodePattern)) {
            throw IllegalArgumentException("Invalid country code")
        }
        if (!phoneNumber.matches(phoneNumberPattern)) {
            throw IllegalArgumentException("Invalid phone number")
        }
    }

    companion object {
        private val countryCodePattern = Regex("^\\d{2}$")
        private val phoneNumberPattern = Regex("^\\d{8,12}$")
    }
}