package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.metadata.users.PermissionsDto

@Serializable
data class InvitationDto(
    val email: String,
    val permissions: PermissionsDto,
    val nickname: String? = null,
    val alertsConfig: JsonObject? = null,
)
