package org.tidepool.sdk.repository

import org.tidepool.sdk.api.AlertApi
import org.tidepool.sdk.dto.alert.AlertConfigDto
import org.tidepool.sdk.dto.alert.toDomain
import org.tidepool.sdk.dto.alert.toDto
import org.tidepool.sdk.model.alert.AlertConfig
import org.tidepool.sdk.runCatchingNetworkExceptions

class AlertRepositoryImpl(
    private val alertApi: AlertApi,
) : AlertRepository {
    
    override suspend fun getAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String
    ): Result<AlertConfig> = runCatchingNetworkExceptions {
        alertApi.getAlertsConfiguration(
            sessionToken = sessionToken,
            userId = userId,
            followerUserId = followerUserId
        )
    }.map { it: AlertConfigDto ->
        it.toDomain() }
    
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
        followerUserId: String
    ): Result<Unit> = runCatchingNetworkExceptions {
        alertApi.deleteAlertsConfiguration(
            sessionToken = sessionToken,
            userId = userId,
            followerUserId = followerUserId
        )
    }
}