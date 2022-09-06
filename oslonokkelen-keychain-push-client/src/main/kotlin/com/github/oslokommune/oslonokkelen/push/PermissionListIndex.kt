package com.github.oslokommune.oslonokkelen.push

/**
 * 
 */
data class PermissionListIndex(val lists: List<Entry>) : Iterable<PermissionListIndex.Entry> {

    data class Entry(
        val permissionListId: PermissionListId,
        val version: Long
    )

    override fun iterator(): Iterator<Entry> {
        return lists.iterator()
    }

}