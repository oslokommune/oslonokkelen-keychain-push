package com.github.oslokommune.oslonokkelen.push

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI
import java.time.Instant
import java.time.temporal.ChronoUnit.SECONDS

internal class ProtoMarshallerTest {

    @Test
    fun `Can re-create requests`() {
        val request = PermissionList.build("booking-123", "Booking #123") {
            addRecipientByPhoneNumber("47", "12345789", false)
            externalLink("More information", URI.create("https://vg.no")) // Optional
            additionalInformation("Tørk av deg på beina før du går inn!") // Optional

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
        val request = PermissionList.build("booking-123", "Booking #123") {
            addRecipientByPhoneNumber("47", "12345789", true)
            addRecipientByPhoneNumber("47", "32132321", false)

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
                    ),
                    pushedAt = Instant.now().truncatedTo(SECONDS),
                    canShare = false
                )
            ),
            confirmedRecipients = listOf(
                PermissionState.ConfirmedRecipient(
                    phoneNumber = PhoneNumber(
                        countryCode = "47",
                        phoneNumber = "32154987"
                    ),
                    usageCounter = 2,
                    confirmedAt = Instant.now().truncatedTo(SECONDS),
                    pushedAt = Instant.now().truncatedTo(SECONDS),
                    canShare = true
                )
            ),
            version = 2,
            link = Link("VG", URI.create("https://vg.no")),
            information = Information("Halla")
        )

        val message = ProtoMarshaller.toProtobuf(state)
        val restoredState = ProtoMarshaller.fromProtobuf(message)

        assertEquals(state, restoredState)
    }

    @Test
    fun `Can restore index message`() {
        val index = PermissionsIndex(
            entries = listOf(
                PermissionsIndex.Entry(
                    id = PermissionListId("123"),
                    version = 2
                ),
                PermissionsIndex.Entry(
                    id = PermissionListId("321"),
                    version = 3
                )
            )
        )

        val message = ProtoMarshaller.toProtobuf(index)
        val restoredIndex = ProtoMarshaller.fromProtobuf(message)

        assertEquals(index, restoredIndex)
    }

}