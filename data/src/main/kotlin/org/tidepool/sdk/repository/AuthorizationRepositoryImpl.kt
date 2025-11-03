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
        groupId: String
    ): Result<Map<String, Set<Permission>>> = runCatchingNetworkExceptions {
        authorizationApi.getUsersInGroup(
            sessionToken = sessionToken,
            groupId = groupId,
        )
    }.map { map ->
        map.mapValues {
            it.value.permissionsSet.map(PermissionDto::toDomain).toSet()
        }
    }
    
    override suspend fun getPermissionsForUser(
        sessionToken: String,
        groupId: String,
        userId: String
    ): Result<Set<Permission>> = runCatchingNetworkExceptions {
        authorizationApi.getPermissionsForUser(
            sessionToken = sessionToken,
            groupId = groupId,
            userId = userId,
        )
    }.map { it.permissionsSet.map { it.toDomain() }.toSet() }
    
    override suspend fun grantPermissionsInGroup(
        sessionToken: String,
        groupId: String,
        userId: String,
        permissions: ModifyUserPermissionsRequest
    ): Result<Set<Permission>> = runCatchingNetworkExceptions {
        authorizationApi.grantPermissionsInGroup(
            sessionToken = sessionToken,
            groupId = groupId,
            userId = userId,
            permissions = permissions.toDto(),
        )
    }.map { it.permissionsSet.map { it.toDomain() }.toSet() }
}