package org.tidepool.sdk.api

import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.metadata.UserProfileDto
import org.tidepool.sdk.dto.metadata.users.TrustUserDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface MetadataApi {
    
    @GET("metadata/collections")
    suspend fun getMetadataCollections(
        @Header("X-Tidepool-Session-Token") sessionToken: String
    ): List<String>
    
    @GET("metadata/{userId}/{collectionName}")
    suspend fun getUserMetadataCollection(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("collectionName") collectionName: String
    ): JsonObject
    
    @POST("metadata/{userId}/{collectionName}")
    suspend fun updateUserMetadataCollectionPost(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("collectionName") collectionName: String,
        @Body collection: JsonObject
    ): JsonObject
    
    @PUT("metadata/{userId}/{collectionName}")
    suspend fun updateUserMetadataCollectionPut(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("collectionName") collectionName: String,
        @Body collection: JsonObject
    ): JsonObject
    
    @GET("metadata/{userId}/private/{fieldName}")
    suspend fun getUserPrivateMetadataItem(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("fieldName") fieldName: String
    ): JsonObject
    
    @GET("metadata/users/{userId}/users")
    suspend fun getTrustUsers(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): List<TrustUserDto>
    
    @GET("metadata/{userId}/profile")
    suspend fun getUserProfile(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): UserProfileDto
}