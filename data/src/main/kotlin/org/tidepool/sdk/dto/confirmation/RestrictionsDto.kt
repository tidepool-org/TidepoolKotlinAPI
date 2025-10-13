package org.tidepool.sdk.dto.confirmation

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.confirmation.ConfirmationStatus
import org.tidepool.sdk.model.confirmation.Restrictions

@Serializable
@KonvertTo(value = Restrictions::class, mapFunctionName = "toDomain")
data class RestrictionsDto(
    @SerialName("canAccept")
    val canAccept: Boolean,
    @SerialName("requiredIdp")
    val requiredIdp: String?
)