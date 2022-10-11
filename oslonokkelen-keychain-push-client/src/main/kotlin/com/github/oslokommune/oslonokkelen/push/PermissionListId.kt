package com.github.oslokommune.oslonokkelen.push

/**
 * The permission id is generated on the client side. It has to be unique per client.
 * We recommend using a reservation number or any other identifier that already exists
 * in your system. Having the id generated on the client side makes it easy to make
 * the endpoint idempotent / avoid double-posting problems.
 */
@JvmInline
value class PermissionListId(val id: String) {
    init {
        if (!id.matches(pattern)) {
            throw IllegalArgumentException("Invalid permission id: $id")
        }
    }

    override fun toString(): String {
        return "Permission id: $id"
    }

    companion object {
        private val pattern = Regex("^[a-z0-9\\-_]{1,40}$")
    }
}