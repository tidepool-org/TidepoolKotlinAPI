package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.confirmation.Restrictions

@Serializable
data class RestrictionsDto(
    @SerialName("canAccept")
    val canAccept: Boolean,
    @SerialName("requiredIdp")
    val requiredIdp: String?
)

fun RestrictionsDto.toDomain(): Restrictions = Restrictions(
    canAccept = canAccept,
    requiredIdp = requiredIdp
)