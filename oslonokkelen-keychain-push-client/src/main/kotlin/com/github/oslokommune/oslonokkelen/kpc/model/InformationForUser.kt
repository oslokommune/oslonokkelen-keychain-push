package com.github.oslokommune.oslonokkelen.kpc.model

import java.net.URI

data class InformationForUser(
        val moreInfoUri: URI? = null,
        val information: String? = null,
        val informationIsMarkdown: Boolean = false
)