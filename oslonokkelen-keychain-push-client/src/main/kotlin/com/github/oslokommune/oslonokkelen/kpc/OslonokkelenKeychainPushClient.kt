package com.github.oslokommune.oslonokkelen.kpc

import com.github.oslokommune.oslonokkelen.keychainpush.proto.KeychainPushApi
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryInfo
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainPushRequest
import java.net.URI

interface OslonokkelenKeychainPushClient {

    suspend fun pullFactoryInfo(factoryId: KeychainFactoryId): KeychainFactoryInfo

    suspend fun push(keychainId: KeychainId, request: KeychainPushRequest)


    /**
     * @param baseUri Where the service is running
     * @param clientId Identifies your system
     * @param clientApiKey Keep this secret
     */
    class Config(
            val baseUri: URI = URI.create("https://citykey-api.k8s.oslo.kommune.no"),
            val clientId: String,
            val clientApiKey: String
    ) {

        private val baseUriStr = baseUri.toString().removeSuffix("/")

        fun formatKeychainFactoryInfoUri(id: KeychainFactoryId): URI {
            return URI.create("$baseUri/api/keychainfactory/${id.value}")
        }

    }


    companion object {
        const val clientIdHeaderName: String = "X-System-Id"
        const val clientApiKeyHeaderName: String = "X-System-Api-Key"
        const val traceIdHeaderName: String = "X-Trace-Id"
    }

    sealed class ClientException(message: String, cause: Exception? = null) : RuntimeException(message, cause) {

        class Unknown(
                message: String,
                cause: Exception)
            : ClientException(message, cause)

        class ErrorResponse(
                val errorCode: KeychainPushApi.ErrorResponse.ErrorCode,
                val technicalDebugMessage: String,
                val traceId: String?
        ) : ClientException("Error response ${errorCode}: $technicalDebugMessage [traceid: ${traceId ?: "none"}]")

    }

}