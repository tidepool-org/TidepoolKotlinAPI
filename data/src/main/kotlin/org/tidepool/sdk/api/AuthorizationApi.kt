package org.tidepool.sdk.api

import org.tidepool.sdk.dto.auth.ModifyUserPermissionsDto
import org.tidepool.sdk.dto.metadata.users.PermissionsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Authorization API interface based on the Tidepool Authorization API specification
 */
interface AuthorizationApi {
    
    /**
     * Retrieve all groups accessible to the user `userId`
     */
    @GET("/access/groups/{userId}")
    suspend fun getGroupsForUser(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): Map<String, PermissionsDto>
    
    /**
     * Retrieve all users that have access to group `groupId`
     */
    @GET("/access/{groupId}")
    suspend fun getUsersInGroup(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("groupId") groupId: String
    ): Map<String, PermissionsDto>
    
    /**
     * Retrieve permissions of individual user `userId` in group `groupId`
     */
    @GET("/access/{groupId}/{userId}")
    suspend fun getPermissionsForUser(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("groupId") groupId: String,
        @Path("userId") userId: String
    ): PermissionsDto
    
    /**
     * Update permissions of individual user `userId` in group `groupId`.
     * The permissions provided in the request body replace all existing permissions for that user.
     * Therefore to delete a permission, submit the request body without that permission.
     */
    @POST("/access/{groupId}/{userId}")
    suspend fun grantPermissionsInGroup(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("groupId") groupId: String,
        @Path("userId") userId: String,
        @Body permissions: ModifyUserPermissionsDto
    ): PermissionsDto
}