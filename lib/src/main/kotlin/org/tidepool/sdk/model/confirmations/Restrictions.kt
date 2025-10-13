package org.tidepool.sdk.model.confirmations

import io.mcarle.konvert.api.KonvertFrom
import org.tidepool.sdk.dto.confirmation.RestrictionsDto

data class Restrictions(
    val canAccept: Boolean,
    val requiredIdp: String?
) {
    @KonvertFrom(RestrictionsDto::class)
    companion object
}