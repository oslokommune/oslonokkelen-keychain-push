package com.github.oslokommune.oslonokkelen.push.index

import com.github.oslokommune.oslonokkelen.push.PermissionId

/**
 * 
 */
data class PermissionIndex(val permissionEntries: List<Entry>) : Iterable<PermissionIndex.Entry> {

    data class Entry(
        val permissionId: PermissionId,
        val version: Long
    )

    override fun iterator(): Iterator<Entry> {
        return permissionEntries.iterator()
    }

}