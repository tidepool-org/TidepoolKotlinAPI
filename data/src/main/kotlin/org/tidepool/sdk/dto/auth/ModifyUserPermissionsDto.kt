package org.tidepool.sdk.dto.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Permissions to be modified for a user.
 * Based on OpenAPI spec's modifyuserpermissions.v1 schema.
 * Only view, note, and upload permissions can be modified (not root or custodian).
 */
@Serializable
data class ModifyUserPermissionsDto(
    val view: JsonObject? = null,
    val note: JsonObject? = null,
    val upload: JsonObject? = null,
)