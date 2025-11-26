package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.model.auth.ModifyUserPermissionsRequest
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
    suspend fun getGroupsForUser(userId: String): Result<Map<String, Set<Permission>>> =
        tokenProvider.getToken().flatMap {
            repository.getGroupsForUser(
                sessionToken = it,
                userId = userId,
            )
        }
    
    /**
     * Retrieve all users that have access to a group
     */
    suspend fun getUsersInGroup(groupId: String): Result<Map<String, Set<Permission>>> =
        tokenProvider.getToken().flatMap {
            repository.getUsersInGroup(
                sessionToken = it,
                groupId = groupId,
            )
        }
    
    /**
     * Retrieve permissions of an individual user in a group
     */
    suspend fun getPermissionsForUser(
        groupId: String,
        userId: String,
    ): Result<Set<Permission>> = tokenProvider.getToken().flatMap {
        repository.getPermissionsForUser(
            sessionToken = it,
            groupId = groupId,
            userId = userId,
        )
    }
    
    /**
     * Update permissions of an individual user in a group.
     * The permissions provided replace all existing permissions for that user.
     * To delete a permission, don't include it in the permissions set.
     */
    suspend fun grantPermissionsInGroup(
        groupId: String,
        userId: String,
        permissions: Set<Permission>,
    ): Result<Set<Permission>> = tokenProvider.getToken().flatMap {
        repository.grantPermissionsInGroup(
            sessionToken = it,
            groupId = groupId,
            userId = userId,
            permissions = ModifyUserPermissionsRequest(permissions),
        )
    }
}
