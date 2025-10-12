package org.tidepool.sdk.repository

import org.tidepool.sdk.api.GeneralApi
import org.tidepool.sdk.dto.general.MinimumClientVersionsDto
import org.tidepool.sdk.dto.general.toDomain
import org.tidepool.sdk.model.general.MinimumClientVersions
import org.tidepool.sdk.runCatchingNetworkExceptions

class GeneralRepositoryImpl(
    private val generalApi: GeneralApi,
) : GeneralRepository {
    
    override suspend fun getMinimumClientVersions() = runCatchingNetworkExceptions {
        generalApi.getMinimumClientVersions()
    }.map { it.toDomain() }
}