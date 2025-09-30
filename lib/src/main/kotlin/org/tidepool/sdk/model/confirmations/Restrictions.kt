package org.tidepool.sdk.model.confirmations

import org.tidepool.sdk.dto.confirmation.RestrictionsDto

data class Restrictions(
    val canAccept: Boolean,
    val requiredIdp: String?
)

internal fun Restrictions.toDto() = RestrictionsDto(
    canAccept = canAccept,
    requiredIdp = requiredIdp,
)

internal fun RestrictionsDto.toDomain() = Restrictions(
    canAccept = canAccept,
    requiredIdp = requiredIdp,
)