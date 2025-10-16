package org.tidepool.sdk.repository

import org.tidepool.sdk.model.auth.ModifyUserPermissionsRequest
import org.tidepool.sdk.model.metadata.users.Permission

/**
 * Repository interface for authorization operations
 */
interface AuthorizationRepository {
    
    /**
     * Retrieve all groups accessible to the user
     */
    suspend fun getGroupsForUser(
        sessionToken: String,
        userId: String
    ): Result<Map<String, Set<Permission>>>
    
    /**
     * Retrieve all users that have access to a group
     */
    suspend fun getUsersInGroup(
        sessionToken: String,
        groupId: String
    ): Result<Map<String, Set<Permission>>>
    
    /**
     * Retrieve permissions of an individual user in a group
     */
    suspend fun getPermissionsForUser(
        sessionToken: String,
        groupId: String,
        userId: String
    ): Result<Set<Permission>>
    
    /**
     * Update permissions of an individual user in a group
     */
    suspend fun grantPermissionsInGroup(
        sessionToken: String,
        groupId: String,
        userId: String,
        permissions: ModifyUserPermissionsRequest,
    ): Result<Set<Permission>>
}