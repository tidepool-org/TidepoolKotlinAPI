package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.metadata.users.PermissionsDto

@Serializable
data class InvitationDto(
    @SerialName("email")
    val email: String,
    @SerialName("permissions")
    val permissions: PermissionsDto,
    @SerialName("nickname")
    val nickname: String? = null,
    @SerialName("alertsConfig")
    val alertsConfig: JsonObject? = null,
)
