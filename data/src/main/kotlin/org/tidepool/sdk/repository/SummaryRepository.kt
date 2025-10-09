package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.summary.SummaryDto

interface SummaryRepository {
    
    suspend fun getSummary(
        sessionToken: String,
        summaryType: String,
        userId: String,
    ): Result<SummaryDto>
}