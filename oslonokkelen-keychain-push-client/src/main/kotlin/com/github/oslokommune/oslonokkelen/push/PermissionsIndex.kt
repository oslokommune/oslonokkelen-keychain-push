package com.github.oslokommune.oslonokkelen.push

/**
 * @param entries Id and version for each permission list your system has pushed to Oslonøkkelen.
 */
data class PermissionsIndex(
    val entries: List<Entry>
) {

    /**
     * @param id Identifies the permission list - Use this to query updated state
     * @param version This will increment every time something happens to the list (recipient attached to profile etc.)
     */
    data class Entry(
        val id: PermissionListId,
        val version: Int
    )
}