package com.github.oslokommune.oslonokkelen.push.permission

import com.github.oslokommune.oslonokkelen.push.AssetId

/**
 * One permission can contain one or more grants. Each grant will grant access to
 * one or more assets within a time interval.
 */
data class Grant(
    val timeInterval: TimeInterval,
    val assetIds: List<AssetId>
)