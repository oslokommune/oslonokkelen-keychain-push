package com.github.oslokommune.oslonokkelen.push

import java.net.URI

/**
 * @param baseUri Where the service is running
 * @param systemId Identifies your system
 * @param apiSecret Keep this secret
 */
class OslonokkelenClientConfig(
    val baseUri: URI,
    val systemId: String,
    val apiSecret: String
) {

    private val normalizedUri = baseUri.toString().removeSuffix("/")

    val systemInfoUri = "$normalizedUri/api/push/info"
    val pushUri = "$normalizedUri/api/push/sync"

    fun stateUri(id: PermissionListId) : String {
        return "$normalizedUri/api/push/state/${id.id}"
    }

}