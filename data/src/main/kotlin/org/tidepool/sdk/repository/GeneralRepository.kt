package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.general.MinimumClientVersionsDto

interface GeneralRepository {
    
    suspend fun getMinimumClientVersions(): Result<MinimumClientVersionsDto>
}