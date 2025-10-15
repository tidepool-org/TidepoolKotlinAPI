package org.tidepool.sdk.repository

import org.tidepool.sdk.model.metadata.users.TrustUser

interface MetadataRepository {
    
    suspend fun getTrustUsers(sessionToken: String): Result<List<TrustUser>>
    
}
