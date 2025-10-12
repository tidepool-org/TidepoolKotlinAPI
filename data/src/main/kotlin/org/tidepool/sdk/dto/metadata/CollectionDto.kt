package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CollectionDto(
    @SerialName("profile")
    val profile: UserProfileDto? = null,
    @SerialName("preferences")
    val preferences: PreferencesDto? = null,
    @SerialName("settings")
    val settings: SettingsDto? = null,
    @SerialName("groups")
    val groups: JsonObject? = null,
    @SerialName("private")
    val private: PrivateDto? = null,
)