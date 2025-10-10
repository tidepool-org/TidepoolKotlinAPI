package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.Serializable

@Serializable
data class SettingsDto(
    val bgTarget: BgTargetDto? = null,
) {
    
    @Serializable
    data class BgTargetDto(
        val low: Double? = null,
        val high: Double? = null,
    )
}