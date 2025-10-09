package org.tidepool.sdk.model.confirmations

import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.confirmation.InvitationDto
import org.tidepool.sdk.dto.metadata.users.PermissionsDto
import org.tidepool.sdk.model.metadata.users.Permission
import org.tidepool.sdk.model.metadata.users.toDomain

data class Invitation(
    val email: String,
    val permissions: Set<Permission>,
    val nickname: String? = null,
    val alertsConfig: JsonObject? = null,
)

internal fun Invitation.toDto(): InvitationDto = InvitationDto(
    email = email,
    permissions = PermissionsDto(
        custodian = JsonObject(emptyMap()).takeIf { permissions.contains(Permission.Custodian) },
        view = JsonObject(emptyMap()).takeIf { permissions.contains(Permission.View) },
        note = JsonObject(emptyMap()).takeIf { permissions.contains(Permission.Note) },
        upload = JsonObject(emptyMap()).takeIf { permissions.contains(Permission.Upload) },
    ),
    nickname = nickname,
    alertsConfig = alertsConfig,
)

internal fun InvitationDto.toDomain(): Invitation = Invitation(
    email = email,
    permissions = permissions.permissionsSet.map { it.toDomain() }.toSet(),
    nickname = nickname,
    alertsConfig = alertsConfig,
)
