package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.model.summary.Summary
import org.tidepool.sdk.model.summary.SummaryType
import org.tidepool.sdk.repository.SummaryRepository
import org.tidepool.sdk.repository.UserRepository

class SummaryService internal constructor(
    private val summaryRepository: SummaryRepository,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) {
    
    suspend fun getSummary(summaryType: SummaryType, userId: String): Result<Summary> =
        tokenProvider.getToken()
            .let { token ->
                summaryRepository.getSummary(
                    sessionToken = token,
                    summaryType = summaryType.toApiString(),
                    userId = userId,
                )
            }
    
    private fun SummaryType.toApiString(): String = when (this) {
        SummaryType.Cgm -> "cgm"
        SummaryType.Bgm -> "bgm"
        SummaryType.Continuous -> "con"
    }
}