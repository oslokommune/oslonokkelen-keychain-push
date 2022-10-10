package com.github.oslokommune.oslonokkelen.push

/**
 * The permission id is generated on the client side. It has to be unique per client.
 * We recommend using a reservation number or any other identifier that already exists
 * in your system. Having the id generated on the client side makes it easy to make
 * the endpoint idempotent / avoid double-posting problems.
 */
@JvmInline
value class PermissionListId(val id: String) {
    override fun toString(): String {
        return "Permission id: $id"
    }
}