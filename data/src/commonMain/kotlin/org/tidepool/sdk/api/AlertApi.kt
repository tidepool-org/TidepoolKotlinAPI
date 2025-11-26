package org.tidepool.sdk.api

import org.tidepool.sdk.dto.alert.AlertConfigDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface AlertApi {
    
    @GET("v1/users/{userId}/followers/{followerUserId}/alerts")
    suspend fun getAlertsConfiguration(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("followerUserId") followerUserId: String
    ): AlertConfigDto
    
    @POST("v1/users/{userId}/followers/{followerUserId}/alerts")
    suspend fun upsertAlertsConfiguration(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("followerUserId") followerUserId: String,
        @Body alertsConfig: AlertConfigDto
    )
    
    @DELETE("v1/users/{userId}/followers/{followerUserId}/alerts")
    suspend fun deleteAlertsConfiguration(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("followerUserId") followerUserId: String
    )
}