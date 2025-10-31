package org.tidepool.sdk.repository

import org.tidepool.sdk.model.summary.Summary


interface SummaryRepository {
    suspend fun getSummary(
        sessionToken: String,
        summaryType: String,
        userId: String,
    ): Result<Summary>
}