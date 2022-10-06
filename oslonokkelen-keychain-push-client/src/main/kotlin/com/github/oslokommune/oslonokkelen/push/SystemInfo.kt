package com.github.oslokommune.oslonokkelen.push

/**
 * Meta information about your system.
 *
 * @param assetIds List of the asset ids your system is allowed to push permission for.
 */
data class SystemInfo(
    val assetIds: List<AssetId>
)