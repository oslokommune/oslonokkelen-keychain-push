package com.github.oslokommune.oslonokkelen.push

/**
 * Identifies what the recipient will be granted access to via Oslonøkkelen.
 * This can be any number of things...
 *
 *  - A single door
 *  - A group of doors
 *  - Ventilation / heating
 *  - Lights
 *
 * Obs!
 * One asset _may_ grant access to more than one thing in Oslonøkkelen.
 *
 * Example: An asset representing a meeting room can grant access both to
 *          the meeting room and any doors you have to pass through
 *          in order to get to the meeting room.
 */
@JvmInline
value class AssetId(val id: String) {
    init {
        if (!id.matches(pattern)) {
            throw IllegalArgumentException("Invalid asset id: $id")
        }
    }

    override fun toString(): String {
        return "Asset id: $id"
    }

    companion object {
        private val pattern = Regex("^[a-z0-9\\-_]{1,20}$")
    }
}