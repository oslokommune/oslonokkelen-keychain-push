package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.sync

import kotlinx.serialization.Serializable

@Serializable
data class PermissionModel(
    val id: String,
    val recipients: List<PhoneNumber>,
    val permissions: List<Permission>
) {

    @Serializable
    data class PhoneNumber(
        val countryCode: String,
        val phoneNumber: String
    )

    @Serializable
    data class Permission(
        val assetIds: List<String>,
        val fromDate: String,
        val untilDate: String,
        val fromTime: String,
        val untilTime: String
    )

}