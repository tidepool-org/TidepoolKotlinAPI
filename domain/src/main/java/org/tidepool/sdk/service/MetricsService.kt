package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.repository.MetricsRepository

class MetricsService internal constructor(
    private val metricsRepository: MetricsRepository,
    private val tokenProvider: TokenProvider,
) {
    
    /**
     * Records a metrics event for a specific user.
     * This method doesn't require authentication but can optionally include a session token.
     */
    suspend fun recordMetricsEventForUser(
        userId: String,
        eventName: String,
        queryParameters: Map<String, String> = emptyMap(),
        includeSessionToken: Boolean = false
    ): Result<Unit> = metricsRepository.recordMetricsEventForUser(
        userId = userId,
        eventName = eventName,
        sessionToken = if (includeSessionToken) tokenProvider.getToken().getOrNull() else null,
        queryParameters = queryParameters
    )
    
    /**
     * Records a metrics event for the currently logged-in user.
     * This method requires authentication.
     */
    suspend fun recordMetricsEventForLoggedInUser(
        eventName: String,
        queryParameters: Map<String, String> = emptyMap()
    ): Result<Unit> = tokenProvider.getToken().flatMap {
        metricsRepository.recordMetricsEventForLoggedInUser(
            sessionToken = it,
            eventName = eventName,
            queryParameters = queryParameters
        )
    }
    
    /**
     * Records a metrics event for a server.
     * This method requires server authentication.
     */
    suspend fun recordMetricsEventForServer(
        serverName: String,
        eventName: String,
        queryParameters: Map<String, String> = emptyMap()
    ): Result<Unit> = tokenProvider.getToken().flatMap {
        metricsRepository.recordMetricsEventForServer(
            sessionToken = it,
            serverName = serverName,
            eventName = eventName,
            queryParameters = queryParameters
        )
    }
}