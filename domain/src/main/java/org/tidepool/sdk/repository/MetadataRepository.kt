package org.tidepool.sdk.repository

import org.tidepool.sdk.model.metadata.UserProfile
import org.tidepool.sdk.model.metadata.users.TrustUser

interface MetadataRepository {
    
    suspend fun getMetadataCollections(
        sessionToken: String
    ): Result<List<String>>
    
    suspend fun getUserMetadataCollection(
        sessionToken: String,
        userId: String,
        collectionName: String
    ): Result<Map<String, Any>>
    
    suspend fun updateUserMetadataCollection(
        sessionToken: String,
        userId: String,
        collectionName: String,
        collection: UserProfile,
        usePost: Boolean = false
    ): Result<Map<String, Any>>
    
    suspend fun getUserPrivateMetadataItem(
        sessionToken: String,
        userId: String,
        fieldName: String
    ): Result<Map<String, Any>>
    
    suspend fun getTrustUsers(
        sessionToken: String,
    ): Result<List<TrustUser>>
    
    suspend fun getUserProfile(
        sessionToken: String,
        userId: String
    ): Result<UserProfile>
}
