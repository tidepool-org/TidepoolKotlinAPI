package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.model.auth.GroupPermissions
import org.tidepool.sdk.model.auth.ModifyUserPermissionsRequest
import org.tidepool.sdk.model.auth.toDomain
import org.tidepool.sdk.model.auth.toDto
import org.tidepool.sdk.model.metadata.users.Permission
import org.tidepool.sdk.repository.AuthorizationRepository

/**
 * Service for managing authorization and permissions
 */
class AuthorizationService internal constructor(
    private val repository: AuthorizationRepository,
    private val tokenProvider: TokenProvider,
) {
    
    /**
     * Retrieve all groups accessible to the user
     */
    suspend fun getGroupsForUser(userId: String): Result<GroupPermissions> =
        repository.getGroupsForUser(
            sessionToken = tokenProvider.getToken(),
            userId = userId
        ).map { it.toDomain() }
    
    /**
     * Retrieve all users that have access to a group
     */
    suspend fun getUsersInGroup(sharerId: String): Result<GroupPermissions> =
        repository.getUsersInGroup(
            sessionToken = tokenProvider.getToken(),
            sharerId = sharerId
        ).map { it.toDomain() }
    
    /**
     * Retrieve permissions of an individual user in a group
     */
    suspend fun getPermissionsForUser(
        sharerId: String,
        userId: String
    ): Result<Set<Permission>> = repository.getPermissionsForUser(
        sessionToken = tokenProvider.getToken(),
        sharerId = sharerId,
        userId = userId
    ).map { it.toDomain() }
    
    /**
     * Update permissions of an individual user in a group.
     * The permissions provided replace all existing permissions for that user.
     * To delete a permission, don't include it in the permissions set.
     */
    suspend fun grantPermissionsInGroup(
        sharerId: String,
        userId: String,
        permissions: Set<Permission>
    ): Result<Set<Permission>> = repository.grantPermissionsInGroup(
        sessionToken = tokenProvider.getToken(),
        sharerId = sharerId,
        userId = userId,
        permissions = ModifyUserPermissionsRequest(permissions).toDto()
    ).map { it.toDomain() }
}
