package com.github.oslokommune.oslonokkelen.kpc.serialization

import com.github.oslokommune.oslonokkelen.kpc.model.KeychainDeleteRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class KeychainDeleteSerializerTest {

    @Test
    fun `Can serialize and parse request with reason`() {
        val request = KeychainDeleteRequest(reason = "Oups")
        val protobuf = KeychainDeleteSerializer.toProtobuf(request)

        assertEquals(request, KeychainDeleteSerializer.fromProtobuf(protobuf))
    }

    @Test
    fun `Can serialize and parse request without reason`() {
        val request = KeychainDeleteRequest(reason = null)
        val protobuf = KeychainDeleteSerializer.toProtobuf(request)

        assertEquals(request, KeychainDeleteSerializer.fromProtobuf(protobuf))
    }

}