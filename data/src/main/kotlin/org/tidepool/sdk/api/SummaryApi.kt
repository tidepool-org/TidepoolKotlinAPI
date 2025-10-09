package org.tidepool.sdk.api

import org.tidepool.sdk.dto.summary.SummaryDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SummaryApi {
    
    @GET("/v1/summaries/{summaryType}/{userId}")
    suspend fun getSummary(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("summaryType") summaryType: String,
        @Path("userId") userId: String
    ): SummaryDto
}