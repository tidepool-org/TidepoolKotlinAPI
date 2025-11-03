package org.tidepool.sdk.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MetricsApi {
    
    @GET("/metrics/user/{userId}/{eventName}")
    suspend fun recordMetricsEventForUser(
        @Path("userId") userId: String,
        @Path("eventName") eventName: String,
        @Header("X-Tidepool-Session-Token") sessionToken: String? = null,
        @QueryMap parameters: Map<String, String>,
    )
    
    @GET("/metrics/thisuser/{eventName}")
    suspend fun recordMetricsEventForLoggedInUser(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("eventName") eventName: String,
        @QueryMap parameters: Map<String, String>,
    )
    
    @GET("/metrics/server/{serverName}/{eventName}")
    suspend fun recordMetricsEventForServer(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("serverName") serverName: String,
        @Path("eventName") eventName: String,
        @QueryMap parameters: Map<String, String>,
    )
}