package com.github.oslokommune.oslonokkelen.push

/**
 * One permission can contain one or more grants. Each grant will grant access to
 * one or more assets within a time interval.
 */
data class Permission(
    val timeInterval: TimeInterval,
    val assetIds: List<AssetId>
)