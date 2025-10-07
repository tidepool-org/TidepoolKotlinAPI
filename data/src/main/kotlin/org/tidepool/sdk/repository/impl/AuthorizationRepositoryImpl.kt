package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.AuthorizationApi
import org.tidepool.sdk.dto.auth.ModifyUserPermissionsDto
import org.tidepool.sdk.dto.metadata.users.PermissionsDto
import org.tidepool.sdk.repository.AuthorizationRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

/**
 * Implementation of AuthorizationRepository using the AuthorizationApi
 */
class AuthorizationRepositoryImpl(
    private val authorizationApi: AuthorizationApi
) : AuthorizationRepository {
    
    override suspend fun getGroupsForUser(
        sessionToken: String,
        userId: String
    ): Result<Map<String, PermissionsDto>> = runCatchingNetworkExceptions {
        authorizationApi.getGroupsForUser(sessionToken, userId)
    }
    
    override suspend fun getUsersInGroup(
        sessionToken: String,
        sharerId: String
    ): Result<Map<String, PermissionsDto>> = runCatchingNetworkExceptions {
        authorizationApi.getUsersInGroup(sessionToken, sharerId)
    }
    
    override suspend fun getPermissionsForUser(
        sessionToken: String,
        sharerId: String,
        userId: String
    ): Result<PermissionsDto> = runCatchingNetworkExceptions {
        authorizationApi.getPermissionsForUser(sessionToken, sharerId, userId)
    }
    
    override suspend fun grantPermissionsInGroup(
        sessionToken: String,
        sharerId: String,
        userId: String,
        permissions: ModifyUserPermissionsDto
    ): Result<PermissionsDto> = runCatchingNetworkExceptions {
        authorizationApi.grantPermissionsInGroup(sessionToken, sharerId, userId, permissions)
    }
}