package org.tidepool.sdk.model.auth

import org.tidepool.sdk.model.metadata.users.Permission

/**
 * Request to modify permissions for a user.
 * Only view, note, and upload permissions can be modified.
 */
data class ModifyUserPermissionsRequest(
    val permissions: Set<Permission>
) {
    
    init {
        // Validate that only modifiable permissions are included
        val invalidPermissions = permissions.filter {
            it !in setOf(Permission.View, Permission.Note, Permission.Upload)
        }
        if (invalidPermissions.isNotEmpty()) {
            throw IllegalArgumentException(
                "Invalid permissions: ${invalidPermissions.joinToString { it.name }}. " +
                        "Only view, note, and upload permissions can be modified."
            )
        }
    }
    
    /**
     * Check if a specific permission is included
     */
    fun hasPermission(permission: Permission): Boolean = permissions.contains(permission)
}