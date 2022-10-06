package com.github.oslokommune.oslonokkelen.push

/**
 * Meta information about your system.
 *
 * @param assetIds List of the asset ids your system is allowed to push permission for.
 */
data class SystemInfo(
    val name: String,
    val information: String,
    val assetIds: List<AssetId>
)