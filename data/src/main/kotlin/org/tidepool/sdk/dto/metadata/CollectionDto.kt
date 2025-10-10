package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CollectionDto(
    val profile: UserProfileDto? = null,
    val preferences: PreferencesDto? = null,
    val settings: SettingsDto? = null,
    val groups: JsonObject? = null,
    val private: PrivateDto? = null,
)