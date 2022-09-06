package com.github.oslokommune.oslonokkelen.push


@JvmInline
value class PermissionListId(val id: String) {
    override fun toString(): String {
        return "Permission id: $id"
    }
}