package org.tidepool.sdk.api

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.QueryMap

interface MetricsApi {
    
    @GET("metrics/user/{userId}/{eventName}")
    suspend fun recordMetricsEventForUser(
        @Path("userId") userId: String,
        @Path("eventName") eventName: String,
        @Header("X-Tidepool-Session-Token") sessionToken: String? = null,
        @QueryMap parameters: Map<String, String>,
    )
    
    @GET("metrics/thisuser/{eventName}")
    suspend fun recordMetricsEventForLoggedInUser(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("eventName") eventName: String,
        @QueryMap parameters: Map<String, String>,
    )
    
    @GET("metrics/server/{serverName}/{eventName}")
    suspend fun recordMetricsEventForServer(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("serverName") serverName: String,
        @Path("eventName") eventName: String,
        @QueryMap parameters: Map<String, String>,
    )
}