package org.tidepool.sdk.repository

import org.tidepool.sdk.api.MetadataApi
import org.tidepool.sdk.api.UserApi
import org.tidepool.sdk.dto.metadata.users.toDomain
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.metadata.users.TrustUser
import org.tidepool.sdk.runCatchingNetworkExceptions

class MetadataRepositoryImpl(
    private val userApi: UserApi,
    private val metadataApi: MetadataApi,
) : MetadataRepository {
    
    override suspend fun getTrustUsers(sessionToken: String): Result<List<TrustUser>> = runCatchingNetworkExceptions {
        val userId = userApi.getCurrentUserInfo(sessionToken).userId
        
        metadataApi.getTrustUsers(
            sessionToken = sessionToken,
            userId = userId,
        )
    }.mapList { it.toDomain() }
}