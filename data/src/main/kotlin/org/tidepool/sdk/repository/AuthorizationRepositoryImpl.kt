package org.tidepool.sdk.repository

import org.tidepool.sdk.api.AuthorizationApi
import org.tidepool.sdk.dto.auth.toDto
import org.tidepool.sdk.dto.metadata.users.PermissionDto
import org.tidepool.sdk.dto.metadata.users.toDomain
import org.tidepool.sdk.model.auth.ModifyUserPermissionsRequest
import org.tidepool.sdk.model.metadata.users.Permission
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
    ): Result<Map<String, Set<Permission>>> = runCatchingNetworkExceptions {
        authorizationApi.getGroupsForUser(
            sessionToken = sessionToken,
            userId = userId,
        )
    }.map { map ->
        map.mapValues {
            it.value.permissionsSet.map(PermissionDto::toDomain).toSet()
        }
    }
    
    override suspend fun getUsersInGroup(
        sessionToken: String,
        sharerId: String
    ): Result<Map<String, Set<Permission>>> = runCatchingNetworkExceptions {
        authorizationApi.getUsersInGroup(
            sessionToken = sessionToken,
            sharerId = sharerId,
        )
    }.map { map ->
        map.mapValues {
            it.value.permissionsSet.map(PermissionDto::toDomain).toSet()
        }
    }
    
    override suspend fun getPermissionsForUser(
        sessionToken: String,
        sharerId: String,
        userId: String
    ): Result<Set<Permission>> = runCatchingNetworkExceptions {
        authorizationApi.getPermissionsForUser(
            sessionToken = sessionToken,
            sharerId = sharerId,
            userId = userId,
        )
    }.map { it.permissionsSet.map { it.toDomain() }.toSet() }
    
    override suspend fun grantPermissionsInGroup(
        sessionToken: String,
        sharerId: String,
        userId: String,
        permissions: ModifyUserPermissionsRequest
    ): Result<Set<Permission>> = runCatchingNetworkExceptions {
        authorizationApi.grantPermissionsInGroup(
            sessionToken = sessionToken,
            sharerId = sharerId,
            userId = userId,
            permissions = permissions.toDto(),
        )
    }.map { it.permissionsSet.map { it.toDomain() }.toSet() }
}