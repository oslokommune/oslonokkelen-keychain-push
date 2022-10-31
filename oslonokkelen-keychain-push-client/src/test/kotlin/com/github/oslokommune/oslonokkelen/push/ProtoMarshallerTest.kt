package com.github.oslokommune.oslonokkelen.push

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI
import java.time.Instant
import java.time.temporal.ChronoUnit.SECONDS

internal class ProtoMarshallerTest {

    @Test
    fun `Can re-create requests`() {
        val request = PushRequest.build("booking-123", "Booking #123") {
            addRecipientByPhoneNumber("47", "12345789")
            externalLink("More information", URI.create("https://vg.no")) // Optional
            additionalPlainTextInformation("Tørk av deg på beina før du går inn!") // Optional

            addPermission(
                interval = TimeInterval.parse("2022-01-01", "12:00", "2022-01-05", "13:15"),
                assetIds = listOf("front-door", "back-door")
            )
            addPermission(
                interval = TimeInterval.parse("2022-01-02", "12:00", "2022-01-05", "13:15"),
                assetIds = listOf("kitchen")
            )
        }

        val protobuf = ProtoMarshaller.toProtobuf(request)
        val recreatedRequest = ProtoMarshaller.fromProtobuf(protobuf)

        assertEquals(request, recreatedRequest)
    }

    @Test
    fun `Can re-create request without link and additional information`() {
        val request = PushRequest.build("booking-123", "Booking #123") {
            addRecipientByPhoneNumber("47", "12345789")
            addRecipientByPhoneNumber("47", "32132321")

            addPermission(
                interval = TimeInterval.parse("2022-01-02", "12:00", "2022-01-05", "13:15"),
                assetIds = listOf("kitchen")
            )
        }

        val protobuf = ProtoMarshaller.toProtobuf(request)
        val recreatedRequest = ProtoMarshaller.fromProtobuf(protobuf)

        assertEquals(request, recreatedRequest)
    }

    @Test
    fun `Can restore state message`() {
        val state = PermissionState(
            pendingRecipients = listOf(
                PermissionState.PendingRecipient(
                    phoneNumber = PhoneNumber(
                        countryCode = "47",
                        phoneNumber = "12345789"
                    )
                )
            ),
            confirmedRecipients = listOf(
                PermissionState.ConfirmedRecipient(
                    phoneNumber = PhoneNumber(
                        countryCode = "47",
                        phoneNumber = "32154987"
                    ),
                    usageCounter = 2,
                    confirmedAt = Instant.now().truncatedTo(SECONDS)
                )
            ),
            version = 2,
            informationLink = InformationLink(URI.create("https://vg.no"), "VG"),
            additionalInformation = AdditionalInformation("Halla", AdditionalInformation.Type.PLAIN_TEXT)
        )

        val message = ProtoMarshaller.toProtobuf(state)
        val restoredState = ProtoMarshaller.fromProtobuf(message)

        assertEquals(state, restoredState)
    }

}