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
        if (countryCode.startsWith("0")) {
            throw IllegalArgumentException("Don't prefix the country code with zeros")
        }
    }

    companion object {
        private val countryCodePattern = Regex("^\\d{2,4}$")
        private val phoneNumberPattern = Regex("^\\d{8,12}$")
    }
}