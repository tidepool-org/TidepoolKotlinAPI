package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SettingsDto(
    @SerialName("bgTarget")
    val bgTarget: BgTargetDto? = null,
) {
    
    @Serializable
    data class BgTargetDto(
        @SerialName("low")
        val low: Double? = null,
        @SerialName("high")
        val high: Double? = null,
    )
}