package com.github.oslokommune.oslonokkelen.push

import com.github.oslokommune.oslonokkelen.push.permission.PushRequest

interface OslonokkelenPushClient {

    suspend fun push(request: PushRequest)

    suspend fun delete(id: PermissionId)

    suspend fun pull(id: PermissionId, knownVersion: Long? = null)

    /**
     *
     */
    suspend fun fetchIndex(knownVersion: Long? = null)

}