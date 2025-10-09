package org.tidepool.sdk.model.auth

import org.tidepool.sdk.dto.metadata.users.PermissionsDto
import org.tidepool.sdk.model.metadata.users.Permission

/**
 * Access permissions granted to one or more users within a group.
 * The key for each entry is the Tidepool User ID of that user.
 */
data class GroupPermissions(
    val userPermissions: Map<String, Set<Permission>>
)

/**
 * Maps DTO to domain model
 */
internal fun Map<String, PermissionsDto>.toDomain() = GroupPermissions(
    userPermissions = mapValues { (_, permissions) ->
        permissions.toDomain()
    }
)

internal fun PermissionsDto.toDomain() = buildSet {
    if (root != null) add(Permission.Root)
    if (custodian != null) add(Permission.Custodian)
    if (view != null) add(Permission.View)
    if (note != null) add(Permission.Note)
    if (upload != null) add(Permission.Upload)
}
