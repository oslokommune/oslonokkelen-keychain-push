package com.github.oslokommune.oslonokkelen.kpc.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI
import java.time.LocalDateTime

internal class KeychainPushRequestTest {

    @Test
    fun `Convenient builder should produce same instance as more verbose constructor`() {
        val requestMadeByBuilder = KeychainPushRequest.build("Some booking") {
            accessBetween("2020-05-19T20:00:00", "2020-05-19T22:00:00")
            recipientByPhoneNumber("47", "12312123")
            moreInformationLink("https://info.example.com")
            information("Be nice", isMarkdown = true)
        }

        val requestMadeByConstructor = KeychainPushRequest(
                recipients = listOf(
                        ProfileLookupKey.PhoneNumber(
                                countryCode = "47",
                                number = "12312123"
                        )
                ),
                periods = listOf(
                        Period(
                                from = LocalDateTime.parse("2020-05-19T20:00:00"),
                                until = LocalDateTime.parse("2020-05-19T22:00:00")
                        )
                ),
                informationForUser = InformationForUser(
                        title = "Some booking",
                        moreInfoUri = URI.create("https://info.example.com"),
                        information = "Be nice",
                        informationIsMarkdown = true
                )
        )

        assertEquals(requestMadeByConstructor, requestMadeByBuilder)
    }

}