package com.github.oslokommune.oslonokkelen.kpc.ktor

import com.github.oslokommune.oslonokkelen.keychainpush.proto.KeychainPushApi
import com.github.oslokommune.oslonokkelen.kpc.OslonokkelenKeychainPushClient
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryId
import com.github.oslokommune.oslonokkelen.kpc.model.KeychainFactoryInfo
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.URI
import java.time.ZoneId

internal class OslonokkelenKeychainPushKtorClientTest {

    private val config = OslonokkelenKeychainPushClient.Config(
            baseUri = URI.create("https://test"),
            clientApiKey = "pepperkake-pepperkake-123",
            clientId = "test-system"
    )

    @Test
    fun `Can parse information from structured error response`() {
        HttpMock("/api/keychainfactory/test-factory") {
            respond(
                    KeychainPushApi.ErrorResponse.newBuilder()
                            .setErrorCode(KeychainPushApi.ErrorResponse.ErrorCode.INVALID_CREDENTIALS)
                            .setTechnicalDebugMessage("I don't know you")
                            .build()
                            .toByteArray(),
                    headers = headersOf("Content-Type", "application/protobuf; type=error")
            )
        }.use { mock ->
            val client = OslonokkelenKeychainPushKtorClient(
                    client = mock.client,
                    config = config
            )

            runBlocking {
                val ex = assertThrows<OslonokkelenKeychainPushClient.ClientException.ErrorResponse> {
                    client.pullFactoryInfo(KeychainFactoryId("test-factory"))
                }

                assertThat(ex).hasMessage("Error response INVALID_CREDENTIALS: I don't know you [traceid: none]")
            }
        }
    }

    @Test
    fun `Can handle unstructured error response`() {
        HttpMock("/api/keychainfactory/test-factory") {
            respondError(
                    status = HttpStatusCode.BadGateway,
                    headers = headersOf("X-Trace-Id", "trace-123"),
                    content = "Oups"
            )
        }.use { mock ->
            val client = OslonokkelenKeychainPushKtorClient(
                    client = mock.client,
                    config = config
            )

            runBlocking {
                val ex = assertThrows<OslonokkelenKeychainPushClient.ClientException.ErrorResponse> {
                    client.pullFactoryInfo(KeychainFactoryId("test-factory"))
                }

                assertThat(ex).hasMessage("Error response UNKNOWN: No content type in http response [traceid: trace-123]")
            }
        }
    }

    @Test
    fun `Can extract information from successful response`() {
        HttpMock("/api/keychainfactory/test-factory") {
            respond(
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", "application/protobuf; type=keychain-factory-info"),
                    content = KeychainPushApi.KeychainFactoryPushInfo.newBuilder()
                            .setTimezoneId("Europe/Oslo")
                            .build()
                            .toByteArray()
            )
        }.use { mock ->
            val client = OslonokkelenKeychainPushKtorClient(
                    client = mock.client,
                    config = config
            )

            runBlocking {
                val factoryId = KeychainFactoryId("test-factory")
                val info = client.pullFactoryInfo(factoryId)

                assertThat(info)
                        .isEqualTo(KeychainFactoryInfo(
                                id = factoryId,
                                timezone = ZoneId.of("Europe/Oslo")
                        ))
            }
        }
    }

}