package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.GeneralApi
import org.tidepool.sdk.dto.general.MinimumClientVersionsDto
import org.tidepool.sdk.repository.GeneralRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

class GeneralRepositoryImpl(
    private val generalApi: GeneralApi,
) : GeneralRepository {
    
    override suspend fun getMinimumClientVersions(): Result<MinimumClientVersionsDto> =
        runCatchingNetworkExceptions {
            generalApi.getMinimumClientVersions()
        }
}