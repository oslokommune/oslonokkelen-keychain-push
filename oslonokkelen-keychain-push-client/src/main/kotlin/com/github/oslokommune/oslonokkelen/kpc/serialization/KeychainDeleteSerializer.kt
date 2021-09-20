package com.github.oslokommune.oslonokkelen.kpc.serialization

import com.github.oslokommune.oslonokkelen.keychainpush.proto.KeychainPushApi
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainDeleteRequest

object KeychainDeleteSerializer {

    fun toProtobuf(request: KeychainDeleteRequest): KeychainPushApi.KeychainDeleteRequest {
        val builder = KeychainPushApi.KeychainDeleteRequest.newBuilder()

        if (request.reason != null) {
            builder.humanReadableReason = request.reason
        }

        return builder.build()
    }

    fun fromProtobuf(request: KeychainPushApi.KeychainDeleteRequest): KeychainDeleteRequest {
        return KeychainDeleteRequest(
            reason = if (request.humanReadableReason != "") {
                request.humanReadableReason
            } else {
                null
            }
        )
    }

}