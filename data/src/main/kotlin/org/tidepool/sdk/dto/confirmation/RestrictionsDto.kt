package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.Serializable

@Serializable
data class RestrictionsDto(
    val canAccept: Boolean,
    val requiredIdp: String?
)