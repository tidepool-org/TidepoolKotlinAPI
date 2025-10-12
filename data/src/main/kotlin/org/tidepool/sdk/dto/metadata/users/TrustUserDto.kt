package org.tidepool.sdk.dto.metadata.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.user.UserDto
import org.tidepool.sdk.model.metadata.UserProfile
import org.tidepool.sdk.model.metadata.users.TrustUser
import org.tidepool.sdk.model.metadata.users.TrustUser.TrusteeUser
import org.tidepool.sdk.model.metadata.users.TrustUser.TrustorUser

/**
 * DTO for trust user data that can contain either trustor or trustee permissions
 */
@Serializable
data class TrustUserDto(
    @SerialName("trustorPermissions")
    val trustorPermissions: PermissionsDto? = null,
    @SerialName("trusteePermissions")
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

fun TrustUserDto.toDomain(): TrustUser = when {
    isTrustor -> TrustorUser(
        permissions = permissions.map { it.toDomain() }.toSet(),
        emailVerified = emailVerified,
        emails = emails,
        termsAccepted = termsAccepted,
        userId = userId,
        username = userName,
        roles = roles,
        createdTime = createdTime,
        createdUserId = createdUserId,
        modifiedTime = modifiedTime,
        modifiedUserId = modifiedUserId,
        deletedTime = deletedTime,
        deletedUserId = deletedUserId,
        profile = profile?.let {
            UserProfile(it.fullName)
        }
    )
    
    isTrustee -> TrusteeUser(
        permissions = permissions.map { it.toDomain() }.toSet(),
        emailVerified = emailVerified,
        emails = emails,
        termsAccepted = termsAccepted,
        userId = userId,
        username = userName,
        roles = roles,
        createdTime = createdTime,
        createdUserId = createdUserId,
        modifiedTime = modifiedTime,
        modifiedUserId = modifiedUserId,
        deletedTime = deletedTime,
        deletedUserId = deletedUserId,
        profile = profile?.let {
            UserProfile(it.fullName)
        }
    )
    
    else -> throw IllegalStateException("TrustUserDto must be either trustor or trustee")
}
