package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.dto.metadata.users.TrustUserDto
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.metadata.users.toDomain
import org.tidepool.sdk.repository.MetadataRepository

class MetadataService internal constructor(
    private val repository: MetadataRepository,
    private val tokenProvider: TokenProvider,
) {
    
    suspend fun getTrustUsers() = repository.getTrustUsers(tokenProvider.getToken())
        .mapList(TrustUserDto::toDomain)
}