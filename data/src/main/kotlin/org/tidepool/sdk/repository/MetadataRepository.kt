package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.metadata.users.TrustUserDto

interface MetadataRepository {
    
    suspend fun getTrustUsers(sessionToken: String): Result<List<TrustUserDto>>
    
}
