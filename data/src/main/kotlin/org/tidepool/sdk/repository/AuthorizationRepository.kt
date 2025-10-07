package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.auth.ModifyUserPermissionsDto
import org.tidepool.sdk.dto.metadata.users.PermissionsDto

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
    ): Result<Map<String, PermissionsDto>>
    
    /**
     * Retrieve all users that have access to a group
     */
    suspend fun getUsersInGroup(
        sessionToken: String,
        sharerId: String
    ): Result<Map<String, PermissionsDto>>
    
    /**
     * Retrieve permissions of an individual user in a group
     */
    suspend fun getPermissionsForUser(
        sessionToken: String,
        sharerId: String,
        userId: String
    ): Result<PermissionsDto>
    
    /**
     * Update permissions of an individual user in a group
     */
    suspend fun grantPermissionsInGroup(
        sessionToken: String,
        sharerId: String,
        userId: String,
        permissions: ModifyUserPermissionsDto
    ): Result<PermissionsDto>
}