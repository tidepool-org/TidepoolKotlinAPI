package org.tidepool.sdk.api

import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.metadata.UserProfileDto
import org.tidepool.sdk.dto.metadata.users.TrustUserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MetadataApi {
    
    @GET("/metadata/collections")
    suspend fun getMetadataCollections(
        @Header("X-Tidepool-Session-Token") sessionToken: String
    ): List<String>
    
    @GET("/metadata/{userId}/{collectionName}")
    suspend fun getUserMetadataCollection(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("collectionName") collectionName: String
    ): JsonObject
    
    @POST("/metadata/{userId}/{collectionName}")
    suspend fun updateUserMetadataCollectionPost(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("collectionName") collectionName: String,
        @Body collection: JsonObject
    ): JsonObject
    
    @PUT("/metadata/{userId}/{collectionName}")
    suspend fun updateUserMetadataCollectionPut(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("collectionName") collectionName: String,
        @Body collection: JsonObject
    ): JsonObject
    
    @GET("/metadata/{userId}/private/{fieldName}")
    suspend fun getUserPrivateMetadataItem(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("fieldName") fieldName: String
    ): JsonObject
    
    @GET("/metadata/users/{userId}/users")
    suspend fun getTrustUsers(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): List<TrustUserDto>
    
    @GET("/metadata/{userId}/profile")
    suspend fun getUserProfile(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): UserProfileDto
}