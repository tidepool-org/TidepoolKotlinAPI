package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.time.Instant

@Serializable
data class PreferencesDto(
    @Contextual
    @SerialName("dismissedDexcomConnectBannerTime")
    val dismissedDexcomConnectBannerTime: Instant? = null,
    @SerialName("additionalProperties")
    val additionalProperties: JsonObject? = null,
)