package com.github.oslokommune.oslonokkelen.push

import com.github.oslokommune.oslonokkelen.push.proto.KeychainPushApiV2

class OslonokkelenClientException(
    val errorCode: KeychainPushApiV2.ErrorResponse.ErrorCode,
    val technicalDebugMessage: String,
    val traceId: String?
) : Exception("Error response ${errorCode}: $technicalDebugMessage [trace-id: ${traceId ?: "none"}]")