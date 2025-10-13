package org.tidepool.sdk.dto.confirmation

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.auth.TokenResponse
import org.tidepool.sdk.model.confirmation.ConfirmationLookup

@Serializable
@KonvertTo(value = ConfirmationLookup::class, mapFunctionName = "toDomain")
data class ConfirmationLookupDto(
    @SerialName("key")
    val key: String,
)