package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.dto.alert.AlertConfigDto
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.model.alert.AlertConfig
import org.tidepool.sdk.model.alert.toDomain
import org.tidepool.sdk.model.alert.toDto
import org.tidepool.sdk.repository.AlertRepository
import org.tidepool.sdk.repository.UserRepository

class AlertService internal constructor(
    private val alertRepository: AlertRepository,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) {
    
    suspend fun getAlertsConfiguration(
        followerUserId: String
    ): Result<AlertConfig> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            alertRepository.getAlertsConfiguration(
                sessionToken = token,
                userId = user.userId,
                followerUserId = followerUserId
            )
        }
    }.map(AlertConfigDto::toDomain)
    
    suspend fun upsertAlertsConfiguration(
        followerUserId: String,
        alertConfig: AlertConfig
    ): Result<Unit> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            alertRepository.upsertAlertsConfiguration(
                sessionToken = token,
                userId = user.userId,
                followerUserId = followerUserId,
                alertsConfig = alertConfig.toDto()
            )
        }
    }
    
    suspend fun deleteAlertsConfiguration(
        followerUserId: String
    ): Result<Unit> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            alertRepository.deleteAlertsConfiguration(
                sessionToken = token,
                userId = user.userId,
                followerUserId = followerUserId
            )
        }
    }
}