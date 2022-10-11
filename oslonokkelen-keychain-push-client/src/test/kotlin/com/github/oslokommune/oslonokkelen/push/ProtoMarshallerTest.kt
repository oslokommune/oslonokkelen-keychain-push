package com.github.oslokommune.oslonokkelen.push

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

internal class ProtoMarshallerTest {

    @Test
    fun `Can re-create`() {
        val request = PushRequest.build("test") {
            addRecipientByPhoneNumber("47", "12345789")
            externalLink("More information", URI.create("https://vg.no"))

            addPermission(
                interval = TimeInterval.parse("2022-01-01", "12:00", "2022-01-05", "13:15"),
                assetIds = listOf("x", "y")
            )
        }

        val protobuf = ProtoMarshaller.toProtobuf(request)
        val recreatedRequest = ProtoMarshaller.fromProtobuf(protobuf)

        assertEquals(request, recreatedRequest)
    }

}