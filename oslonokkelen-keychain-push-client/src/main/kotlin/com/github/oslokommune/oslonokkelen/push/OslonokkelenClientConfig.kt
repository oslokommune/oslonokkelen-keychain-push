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

    val systemInfoUri = "$baseUri/api/push/info"

}