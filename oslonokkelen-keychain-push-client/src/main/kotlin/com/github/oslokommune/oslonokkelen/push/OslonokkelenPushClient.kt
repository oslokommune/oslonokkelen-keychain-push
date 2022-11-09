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



}