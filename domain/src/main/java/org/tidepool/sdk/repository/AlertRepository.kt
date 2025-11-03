package org.tidepool.sdk.repository

import org.tidepool.sdk.model.alert.AlertConfig

interface AlertRepository {
    
    suspend fun getAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String
    ): Result<AlertConfig>
    
    suspend fun upsertAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String,
        alertsConfig: AlertConfig
    ): Result<Unit>
    
    suspend fun deleteAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String
    ): Result<Unit>
}