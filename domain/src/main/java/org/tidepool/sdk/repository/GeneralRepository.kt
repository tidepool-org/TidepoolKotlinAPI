package org.tidepool.sdk.repository

import org.tidepool.sdk.model.general.MinimumClientVersions

interface GeneralRepository {
    
    suspend fun getMinimumClientVersions(): Result<MinimumClientVersions>
}