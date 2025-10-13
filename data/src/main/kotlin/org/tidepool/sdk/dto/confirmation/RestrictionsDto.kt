package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestrictionsDto(
    @SerialName("canAccept")
    val canAccept: Boolean,
    @SerialName("requiredIdp")
    val requiredIdp: String?
)