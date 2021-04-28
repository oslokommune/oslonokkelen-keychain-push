package com.github.oslokommune.oslonokkelen.kpc.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath

class HttpMock(private val path: String, private val handler: MockRequestHandleScope.() -> HttpResponseData) :
        AutoCloseable {

    val client: HttpClient = HttpClient(MockEngine) {
        expectSuccess = false

        engine {
            addHandler { request ->
                if (request.url.fullPath == path) {
                    handler(this)
                } else {
                    respondError(
                            status = HttpStatusCode.InternalServerError,
                            content = "TEST ERROR: No handler configured for ${request.url.fullPath}"
                    )
                }
            }
        }
    }

    override fun close() {
        client.close()
    }
}