package com.github.oslokommune.oslonokkelen.push

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

internal class ProtoMarshallerTest {

    @Test
    fun `Can re-create requests`() {
        val request = PushRequest.build("booking-123") {
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
        val request = PushRequest.build("booking-123") {
            addRecipientByPhoneNumber("47", "12345789")

            addPermission(
                interval = TimeInterval.parse("2022-01-02", "12:00", "2022-01-05", "13:15"),
                assetIds = listOf("kitchen")
            )
        }

        val protobuf = ProtoMarshaller.toProtobuf(request)
        val recreatedRequest = ProtoMarshaller.fromProtobuf(protobuf)

        assertEquals(request, recreatedRequest)
    }

}