package com.github.oslokommune.oslonokkelen.push


@JvmInline
value class PermissionId(val id: String) {
    override fun toString(): String {
        return "Permission id: $id"
    }
}