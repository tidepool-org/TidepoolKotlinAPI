package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.MetadataApi
import org.tidepool.sdk.api.UserApi
import org.tidepool.sdk.repository.MetadataRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

class MetadataRepositoryImpl(
    private val userApi: UserApi,
    private val metadataApi: MetadataApi,
) : MetadataRepository {
    
    override suspend fun getTrustUsers(sessionToken: String) = runCatchingNetworkExceptions {
        val userId = userApi.getCurrentUserInfo(sessionToken).userId
        
        metadataApi.getTrustUsers(
            sessionToken = sessionToken,
            userId = userId,
        )
    }
}