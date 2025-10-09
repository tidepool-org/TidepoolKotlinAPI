package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Serializable

@Serializable
data class SiteDto(
    val id: String,
    val name: String,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val state: String? = null,
    val country: String? = null,
)