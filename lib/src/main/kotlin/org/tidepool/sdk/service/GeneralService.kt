package org.tidepool.sdk.service

import org.tidepool.sdk.model.general.MinimumClientVersions
import org.tidepool.sdk.model.general.toDomain
import org.tidepool.sdk.repository.GeneralRepository

class GeneralService internal constructor(
    private val generalRepository: GeneralRepository,
) {
    
    suspend fun getMinimumClientVersions(): Result<MinimumClientVersions> =
        generalRepository.getMinimumClientVersions()
            .map { it.toDomain() }
}