package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.sync

import kotlinx.serialization.Serializable

@Serializable
data class PermissionModel(
    val id: String,
    val title: String,
    val recipients: List<PhoneNumber>,
    val permissions: List<Permission>,
    val information: Information? = null,
    val link: Link? = null
) {

    @Serializable
    data class PhoneNumber(
        val countryCode: String,
        val phoneNumber: String
    )

    @Serializable
    data class Information(
        val content: String
    )

    @Serializable
    data class Link(
        val title: String,
        val uri: String
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