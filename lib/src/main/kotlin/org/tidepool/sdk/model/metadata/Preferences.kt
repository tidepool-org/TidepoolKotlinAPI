package org.tidepool.sdk.model.metadata

import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.metadata.PreferencesDto
import java.time.Instant

data class Preferences(
    val dismissedDexcomConnectBannerTime: Instant? = null,
    val additionalProperties: JsonObject? = null,
)

internal fun Preferences.toDto() = PreferencesDto(
    dismissedDexcomConnectBannerTime = dismissedDexcomConnectBannerTime,
    additionalProperties = additionalProperties,
)

internal fun PreferencesDto.toDomain() = Preferences(
    dismissedDexcomConnectBannerTime = dismissedDexcomConnectBannerTime,
    additionalProperties = additionalProperties,
)