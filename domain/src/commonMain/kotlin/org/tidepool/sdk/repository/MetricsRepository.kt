package org.tidepool.sdk.repository

interface MetricsRepository {
    
    suspend fun recordMetricsEventForUser(
        userId: String,
        eventName: String,
        sessionToken: String? = null,
        queryParameters: Map<String, String> = emptyMap()
    ): Result<Unit>
    
    suspend fun recordMetricsEventForLoggedInUser(
        sessionToken: String,
        eventName: String,
        queryParameters: Map<String, String> = emptyMap()
    ): Result<Unit>
    
    suspend fun recordMetricsEventForServer(
        sessionToken: String,
        serverName: String,
        eventName: String,
        queryParameters: Map<String, String> = emptyMap()
    ): Result<Unit>
}