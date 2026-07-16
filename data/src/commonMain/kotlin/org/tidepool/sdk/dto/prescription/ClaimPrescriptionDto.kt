package org.tidepool.sdk.dto.prescription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClaimPrescriptionDto(
    @SerialName("accessCode") val accessCode: String,
    @SerialName("birthday") val birthday: String    // "yyyy-MM-dd" ISO date — NO time component
)
