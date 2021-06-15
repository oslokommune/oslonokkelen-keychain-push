package com.github.oslokommune.oslonokkelen.kpc.model

sealed class ProfileLookupKey {

    data class PhoneNumber(
            val countryCode: String,
            val number: String
    ) : ProfileLookupKey() {
        init {
            if (countryCode.length != 2 || !countryCode[0].isDigit() || !countryCode[1].isDigit()) {
                throw IllegalArgumentException("Invalid country code '$countryCode' (valid example '47')")
            }
            if (number.length < 8 || number.length > 12) {
                throw IllegalArgumentException("Invalid phone number '$number'")
            }
            for (c in number) {
                if (!c.isDigit()) {
                    throw IllegalArgumentException("Invalid phone number '$number'")
                }
            }
        }
    }

}