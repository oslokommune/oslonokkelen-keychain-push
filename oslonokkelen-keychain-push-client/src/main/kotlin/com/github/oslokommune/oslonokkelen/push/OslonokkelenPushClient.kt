package com.github.oslokommune.oslonokkelen.push

interface OslonokkelenPushClient {

    suspend fun push(request: PushRequest)

    suspend fun delete(id: PermissionListId)

    suspend fun pull(id: PermissionListId, knownVersion: Long? = null)

    /**
     *
     */
    suspend fun fetchIndex(knownVersion: Long? = null)

}