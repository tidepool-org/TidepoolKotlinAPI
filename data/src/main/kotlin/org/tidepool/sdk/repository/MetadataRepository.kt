package org.tidepool.sdk.repository

import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.metadata.UserProfileDto
import org.tidepool.sdk.dto.metadata.users.TrustUserDto

interface MetadataRepository {
    
    suspend fun getMetadataCollections(
        sessionToken: String
    ): Result<List<String>>
    
    suspend fun getUserMetadataCollection(
        sessionToken: String,
        userId: String,
        collectionName: String
    ): Result<JsonObject>
    
    suspend fun updateUserMetadataCollection(
        sessionToken: String,
        userId: String,
        collectionName: String,
        collection: UserProfileDto,
        usePost: Boolean = false
    ): Result<JsonObject>
    
    suspend fun getUserPrivateMetadataItem(
        sessionToken: String,
        userId: String,
        fieldName: String
    ): Result<JsonObject>
    
    suspend fun getTrustUsers(
        sessionToken: String,
    ): Result<List<TrustUserDto>>
    
    suspend fun getUserProfile(
        sessionToken: String,
        userId: String
    ): Result<UserProfileDto>
}
