package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.metadata.users.TrustUser
import org.tidepool.sdk.repository.MetadataRepository

class MetadataService internal constructor(
    private val repository: MetadataRepository,
    private val tokenProvider: TokenProvider,
) {
    
    suspend fun getTrustUsers(): Result<List<TrustUser>> = repository.getTrustUsers(tokenProvider.getToken())
        .mapList { TrustUser.fromDto(it) }
}