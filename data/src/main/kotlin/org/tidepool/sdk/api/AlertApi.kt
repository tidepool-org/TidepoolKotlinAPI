package org.tidepool.sdk.api

import org.tidepool.sdk.dto.alert.AlertConfigDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AlertApi {
    
    @GET("/v1/users/{userId}/followers/{followerUserId}/alerts")
    suspend fun getAlertsConfiguration(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("followerUserId") followerUserId: String
    ): AlertConfigDto
    
    @POST("/v1/users/{userId}/followers/{followerUserId}/alerts")
    suspend fun upsertAlertsConfiguration(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("followerUserId") followerUserId: String,
        @Body alertsConfig: AlertConfigDto
    )
    
    @DELETE("/v1/users/{userId}/followers/{followerUserId}/alerts")
    suspend fun deleteAlertsConfiguration(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("followerUserId") followerUserId: String
    )
}