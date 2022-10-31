package com.github.oslokommune.oslonokkelen.push

/**
 * Meta information about your system.
 *
 * @param name Name of your system. Example: AwesomeBooking AS
 * @param id Identifies your system. This won't change.
 * @param information A short text describing your system, point of contacts etc.
 * @param assetIds List of the asset ids your system is allowed to push permission for.
 */
data class SystemInfo(
    val name: String,
    val id: String,
    val information: String,
    val assetIds: List<AssetId>
)