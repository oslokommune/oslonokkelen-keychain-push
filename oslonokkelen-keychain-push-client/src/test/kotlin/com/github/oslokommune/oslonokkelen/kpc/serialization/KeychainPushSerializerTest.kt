package com.github.oslokommune.oslonokkelen.kpc.serialization

import com.github.oslokommune.oslonokkelen.kpc.model.InformationForUser
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest
import com.github.oslokommune.oslonokkelen.kpc.model.Period
import com.github.oslokommune.oslonokkelen.kpc.model.ProfileLookupKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI
import java.time.LocalDateTime

internal class KeychainPushSerializerTest {

    @Test
    fun `Can convert request with link to protobuf and back`() {
        val request = KeychainPushRequest(
                recipients = listOf(
                        ProfileLookupKey.PhoneNumber(
                                countryCode = "47",
                                number = "12312123"
                        )
                ),
                periods = listOf(
                        Period(
                                from = LocalDateTime.now(),
                                until = LocalDateTime.now().plusDays(1)
                        )
                ),
                informationForUser = InformationForUser(
                        title = "Some booking",
                        moreInfoUri = URI.create("https://www.vg.no"),
                        information = "Nøkkelen ligger *under* matta",
                        informationIsMarkdown = true
                )
        )

        val protobufRequest = KeychainPushSerializer.toProtobuf(request)
        val recreatedRequest = KeychainPushSerializer.fromProtobuf(protobufRequest)

        assertEquals(request, recreatedRequest)
    }

    @Test
    fun `Can convert request without message to protobuf and back`() {
        val request = KeychainPushRequest(
                recipients = listOf(
                        ProfileLookupKey.PhoneNumber(
                                countryCode = "47",
                                number = "12312123"
                        )
                ),
                periods = listOf(
                        Period(
                                from = LocalDateTime.now(),
                                until = LocalDateTime.now().plusDays(1)
                        )
                ),
                informationForUser = InformationForUser(
                        moreInfoUri = URI.create("https://www.vg.no"),
                        title = "Some booking"
                )
        )

        val protobufRequest = KeychainPushSerializer.toProtobuf(request)
        val recreatedRequest = KeychainPushSerializer.fromProtobuf(protobufRequest)

        assertEquals(request, recreatedRequest)
    }

    @Test
    fun `Can convert request without link to protobuf and back`() {
        val request = KeychainPushRequest(
                recipients = listOf(
                        ProfileLookupKey.PhoneNumber(
                                countryCode = "47",
                                number = "12312123"
                        )
                ),
                periods = listOf(
                        Period(
                                from = LocalDateTime.now(),
                                until = LocalDateTime.now().plusDays(1)
                        )
                ),
                informationForUser = InformationForUser(
                        title = "Some booking",
                        information = "Nøkkelen ligger *under* matta",
                        informationIsMarkdown = true
                )
        )

        val protobufRequest = KeychainPushSerializer.toProtobuf(request)
        val recreatedRequest = KeychainPushSerializer.fromProtobuf(protobufRequest)

        assertEquals(request, recreatedRequest)
    }

}