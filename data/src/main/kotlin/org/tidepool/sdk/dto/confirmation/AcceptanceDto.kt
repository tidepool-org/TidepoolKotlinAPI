package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.Serializable

@Serializable
data class AcceptanceDto(
    val password: String,
    val birthday: String, // format: "2012-08-30"
)
