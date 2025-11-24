package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import org.tidepool.sdk.api.AlertApi
import org.tidepool.sdk.di.provideAlertApi
import org.tidepool.sdk.dto.alert.AlertConfigDto
import org.tidepool.sdk.dto.alert.toDomain
import org.tidepool.sdk.dto.alert.toDto
import org.tidepool.sdk.model.alert.AlertConfig
import org.tidepool.sdk.runCatchingNetworkExceptions

class AlertRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
) : AlertRepository {

    private val alertApi: AlertApi
        get() = provideAlertApi(environmentRepository.getKtorfit(httpClient))

    override suspend fun getAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String,
    ): Result<AlertConfig> = runCatchingNetworkExceptions {
        alertApi.getAlertsConfiguration(
            sessionToken = sessionToken,
            userId = userId,
            followerUserId = followerUserId
        )
    }.map(AlertConfigDto::toDomain)

    override suspend fun upsertAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String,
        alertsConfig: AlertConfig,
    ): Result<Unit> = runCatchingNetworkExceptions {
        alertApi.upsertAlertsConfiguration(
            sessionToken = sessionToken,
            userId = userId,
            followerUserId = followerUserId,
            alertsConfig = alertsConfig.toDto(),
        )
    }

    override suspend fun deleteAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        alertApi.deleteAlertsConfiguration(
            sessionToken = sessionToken,
            userId = userId,
            followerUserId = followerUserId
        )
    }
}