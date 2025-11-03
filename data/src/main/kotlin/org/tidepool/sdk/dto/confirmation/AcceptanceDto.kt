package org.tidepool.sdk.dto.confirmation

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.confirmations.Acceptance

@Serializable
@KonvertTo(Acceptance::class, mapFunctionName = "toDomain")
data class AcceptanceDto(
    @SerialName("password")
    val password: String,
    @SerialName("birthday")
    val birthday: String, // format: "2012-08-30"
)
