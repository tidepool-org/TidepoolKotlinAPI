package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.SummaryApi
import org.tidepool.sdk.dto.summary.SummaryDto
import org.tidepool.sdk.repository.SummaryRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

class SummaryRepositoryImpl(
    private val summaryApi: SummaryApi,
) : SummaryRepository {
    
    override suspend fun getSummary(
        sessionToken: String,
        summaryType: String,
        userId: String,
    ): Result<SummaryDto> = runCatchingNetworkExceptions {
        summaryApi.getSummary(
            sessionToken = sessionToken,
            summaryType = summaryType,
            userId = userId,
        )
    }
}