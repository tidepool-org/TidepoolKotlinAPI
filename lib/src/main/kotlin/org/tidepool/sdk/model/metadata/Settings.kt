package org.tidepool.sdk.model.metadata

import org.tidepool.sdk.dto.metadata.SettingsDto

data class Settings(
    val bgTarget: BgTarget? = null,
) {
    
    data class BgTarget(
        val low: Double? = null,
        val high: Double? = null,
    )
}

internal fun Settings.toDto() = SettingsDto(
    bgTarget = bgTarget?.toDto(),
)

internal fun SettingsDto.toDomain() = Settings(
    bgTarget = bgTarget?.toDomain(),
)

internal fun Settings.BgTarget.toDto() = SettingsDto.BgTargetDto(
    low = low,
    high = high,
)

internal fun SettingsDto.BgTargetDto.toDomain() = Settings.BgTarget(
    low = low,
    high = high,
)