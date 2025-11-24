package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import org.tidepool.sdk.api.SummaryApi
import org.tidepool.sdk.di.provideSummaryApi
import org.tidepool.sdk.dto.summary.SummaryDto
import org.tidepool.sdk.dto.summary.toDomain
import org.tidepool.sdk.model.summary.Summary
import org.tidepool.sdk.runCatchingNetworkExceptions

class SummaryRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
) : SummaryRepository {

    private val summaryApi: SummaryApi
        get() = provideSummaryApi(environmentRepository.getKtorfit(httpClient))

    override suspend fun getSummary(
        sessionToken: String,
        summaryType: String,
        userId: String,
    ): Result<Summary> = runCatchingNetworkExceptions {
        summaryApi.getSummary(
            sessionToken = sessionToken,
            summaryType = summaryType,
            userId = userId,
        )
    }.map { it.toDomain() }
}