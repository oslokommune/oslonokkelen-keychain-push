package com.github.oslokommune.oslonokkelen.push

/**
 * This is the v2 version of the push api.
 */
interface OslonokkelenPushClient {

    /**
     * This method can be used both for the original push and later to sync any updates.
     *
     * @param permissionList Push request describing what to grant and whom to grant it.
     */
    suspend fun push(permissionList: PermissionList)

    /**
     * @return A description of your client / system
     */
    suspend fun describeSystem() : SystemInfo

    /**
     * Can be used to figure out if all recipients has received the permission.
     */
    suspend fun queryState(id: PermissionListId) : PermissionState

    /**
     * Fetches a list of all permissions your system has pushed to Oslonøkkelen including a version
     * number. If your system keeps track of the version number this can be used to query the state
     * of updated permissions using the `queryState` method.
     *
     * @return A list of each permission list your system has pushed to Oslonøkkelen including a version field.
     */
    suspend fun index() : PermissionsIndex

    /**
     * Will delete the entire permission list. This will remove all traces of the permission
     * from the apps of the recipients. In some cases like cancellations it might be more appropriate
     * to push an update with all permissions removed and a description explaining that it has been
     * cancelled.
     *
     * @param id The id of the permission list you want to delete. This is the same id used in the push method.
     */
    suspend fun delete(id: PermissionListId)

    companion object {
        const val clientIdHeaderName: String = "X-System-Id"
        const val clientApiKeyHeaderName: String = "X-System-Api-Key"
        const val traceIdHeaderName: String = "X-Trace-Id"
    }

}