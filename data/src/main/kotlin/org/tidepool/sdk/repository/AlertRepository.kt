package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.alert.AlertConfigDto

interface AlertRepository {
    
    suspend fun getAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String
    ): Result<AlertConfigDto>
    
    suspend fun upsertAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String,
        alertsConfig: AlertConfigDto
    ): Result<Unit>
    
    suspend fun deleteAlertsConfiguration(
        sessionToken: String,
        userId: String,
        followerUserId: String
    ): Result<Unit>
}