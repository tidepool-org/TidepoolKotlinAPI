package org.tidepool.sdk.dto.metadata.users

import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.user.UserDto

/**
 * DTO for trust user data that can contain either trustor or trustee permissions
 */
@Serializable
data class TrustUserDto(
    val trustorPermissions: PermissionsDto? = null,
    val trusteePermissions: PermissionsDto? = null
) : UserDto() {
    
    /**
     * Gets the permissions regardless of whether this is a trustor or trustee
     */
    val permissions: Set<PermissionDto>
        get() = trustorPermissions?.permissionsSet ?: trusteePermissions?.permissionsSet
        ?: emptySet()
    
    /**
     * Indicates whether this is a trustor user (has trustorPermissions)
     */
    val isTrustor: Boolean
        get() = trustorPermissions != null
    
    /**
     * Indicates whether this is a trustee user (has trusteePermissions)
     */
    val isTrustee: Boolean
        get() = trusteePermissions != null
    
}