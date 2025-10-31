package org.tidepool.sdk.repository

import org.tidepool.sdk.api.MetricsApi
import org.tidepool.sdk.runCatchingNetworkExceptions

class MetricsRepositoryImpl(
    private val metricsApi: MetricsApi,
) : MetricsRepository {
    
    override suspend fun recordMetricsEventForUser(
        userId: String,
        eventName: String,
        sessionToken: String?,
        queryParameters: Map<String, String>
    ): Result<Unit> = runCatchingNetworkExceptions {
        metricsApi.recordMetricsEventForUser(
            userId = userId,
            eventName = eventName,
            sessionToken = sessionToken,
            parameters = queryParameters,
        )
    }
    
    override suspend fun recordMetricsEventForLoggedInUser(
        sessionToken: String,
        eventName: String,
        queryParameters: Map<String, String>
    ): Result<Unit> = runCatchingNetworkExceptions {
        metricsApi.recordMetricsEventForLoggedInUser(
            sessionToken = sessionToken,
            eventName = eventName,
            parameters = queryParameters,
        )
    }
    
    override suspend fun recordMetricsEventForServer(
        sessionToken: String,
        serverName: String,
        eventName: String,
        queryParameters: Map<String, String>
    ): Result<Unit> = runCatchingNetworkExceptions {
        metricsApi.recordMetricsEventForServer(
            sessionToken = sessionToken,
            serverName = serverName,
            eventName = eventName,
            parameters = queryParameters,
        )
    }
}