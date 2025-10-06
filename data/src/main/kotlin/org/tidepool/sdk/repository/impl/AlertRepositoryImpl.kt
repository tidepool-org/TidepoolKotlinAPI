package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.AlertApi
import org.tidepool.sdk.dto.alert.AlertConfigDto
import org.tidepool.sdk.repository.AlertRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

class AlertRepositoryImpl(
    private val alertApi: AlertApi,
) : AlertRepository {
    
    override suspend fun getAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String
    ): Result<AlertConfigDto> = runCatchingNetworkExceptions {
        alertApi.getAlertsConfiguration(
            sessionToken = sessionToken,
            userId = userId,
            followerUserId = followerUserId
        )
    }
    
    override suspend fun upsertAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String,
        alertsConfig: AlertConfigDto
    ): Result<Unit> = runCatchingNetworkExceptions {
        alertApi.upsertAlertsConfiguration(
            sessionToken = sessionToken,
            userId = userId,
            followerUserId = followerUserId,
            alertsConfig = alertsConfig
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