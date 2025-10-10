package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.time.Instant

@Serializable
data class PreferencesDto(
    @Contextual val dismissedDexcomConnectBannerTime: Instant? = null,
    val additionalProperties: JsonObject? = null,
)