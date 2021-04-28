package com.github.oslokommune.oslonokkelen.kpc.model

sealed class ProfileLookupKey {

    data class PhoneNumber(
            val countryCode: String,
            val number: String
    ) : ProfileLookupKey()

}