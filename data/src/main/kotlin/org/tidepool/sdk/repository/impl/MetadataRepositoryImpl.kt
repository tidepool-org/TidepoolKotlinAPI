package org.tidepool.sdk.repository.impl

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.api.MetadataApi
import org.tidepool.sdk.api.UserApi
import org.tidepool.sdk.dto.metadata.UserProfileDto
import org.tidepool.sdk.dto.metadata.users.TrustUserDto
import org.tidepool.sdk.repository.MetadataRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

class MetadataRepositoryImpl(
    private val userApi: UserApi,
    private val metadataApi: MetadataApi,
) : MetadataRepository {
    
    override suspend fun getMetadataCollections(
        sessionToken: String
    ): Result<List<String>> = runCatchingNetworkExceptions {
        metadataApi.getMetadataCollections(sessionToken = sessionToken)
    }
    
    override suspend fun getUserMetadataCollection(
        sessionToken: String,
        userId: String,
        collectionName: String
    ): Result<JsonObject> = runCatchingNetworkExceptions {
        metadataApi.getUserMetadataCollection(
            sessionToken = sessionToken,
            userId = userId,
            collectionName = collectionName,
        )
    }
    
    override suspend fun updateUserMetadataCollection(
        sessionToken: String,
        userId: String,
        collectionName: String,
        collection: UserProfileDto,
        usePost: Boolean
    ): Result<JsonObject> = runCatchingNetworkExceptions {
        val json = Json.encodeToJsonElement(
            UserProfileDto.serializer(),
            collection,
        ) as JsonObject
        
        if (usePost) {
            metadataApi.updateUserMetadataCollectionPost(
                sessionToken = sessionToken,
                userId = userId,
                collectionName = collectionName,
                collection = json,
            )
        } else {
            metadataApi.updateUserMetadataCollectionPut(
                sessionToken = sessionToken,
                userId = userId,
                collectionName = collectionName,
                collection = json,
            )
        }
    }
    
    override suspend fun getUserPrivateMetadataItem(
        sessionToken: String,
        userId: String,
        fieldName: String
    ): Result<JsonObject> = runCatchingNetworkExceptions {
        metadataApi.getUserPrivateMetadataItem(
            sessionToken = sessionToken,
            userId = userId,
            fieldName = fieldName,
        )
    }
    
    override suspend fun getTrustUsers(
        sessionToken: String,
    ): Result<List<TrustUserDto>> = runCatchingNetworkExceptions {
        metadataApi.getTrustUsers(
            sessionToken = sessionToken,
            userId = userApi.getCurrentUserInfo(sessionToken).userId,
        )
    }
    
    override suspend fun getUserProfile(
        sessionToken: String,
        userId: String
    ): Result<UserProfileDto> = runCatchingNetworkExceptions {
        metadataApi.getUserProfile(
            sessionToken = sessionToken,
            userId = userId,
        )
    }
}