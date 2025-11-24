package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.tidepool.sdk.api.MetadataApi
import org.tidepool.sdk.api.UserApi
import org.tidepool.sdk.di.provideMetadataApi
import org.tidepool.sdk.di.provideUserApi
import org.tidepool.sdk.dto.metadata.toDomain
import org.tidepool.sdk.dto.metadata.toDto
import org.tidepool.sdk.dto.metadata.users.toDomain
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.metadata.UserProfile
import org.tidepool.sdk.model.metadata.users.TrustUser
import org.tidepool.sdk.runCatchingNetworkExceptions

class MetadataRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
) : MetadataRepository {

    private val userApi: UserApi
        get() = provideUserApi(environmentRepository.getKtorfit(httpClient))

    private val metadataApi: MetadataApi
        get() = provideMetadataApi(environmentRepository.getKtorfit(httpClient))

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
        collection: UserProfile,
        usePost: Boolean
    ): Result<JsonObject> = runCatchingNetworkExceptions {
        val json = Json.encodeToJsonElement(
            collection.toDto(),
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
    ): Result<List<TrustUser>> = runCatchingNetworkExceptions {
        metadataApi.getTrustUsers(
            sessionToken = sessionToken,
            userId = userApi.getCurrentUserInfo(sessionToken).userId,
        )
    }.mapList { it.toDomain() }
    
    override suspend fun getUserProfile(
        sessionToken: String,
        userId: String
    ): Result<UserProfile> = runCatchingNetworkExceptions {
        metadataApi.getUserProfile(
            sessionToken = sessionToken,
            userId = userId,
        )
    }.map { it.toDomain() }
}