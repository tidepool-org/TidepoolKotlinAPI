package org.tidepool.sdk.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.model.auth.ModifyUserPermissionsRequest
import org.tidepool.sdk.model.metadata.users.Permission

/**
 * Permissions to be modified for a user.
 * Based on OpenAPI spec's modifyuserpermissions.v1 schema.
 * Only view, note, and upload permissions can be modified (not root or custodian).
 */
@Serializable
data class ModifyUserPermissionsDto(
    @SerialName("view")
    val view: JsonObject? = null,
    @SerialName("note")
    val note: JsonObject? = null,
    @SerialName("upload")
    val upload: JsonObject? = null,
)

internal fun ModifyUserPermissionsRequest.toDto() = ModifyUserPermissionsDto(
    view = JsonObject(emptyMap()).takeIf { hasPermission(Permission.View) },
    note = JsonObject(emptyMap()).takeIf { hasPermission(Permission.Note) },
    upload = JsonObject(emptyMap()).takeIf { hasPermission(Permission.Upload) },
)