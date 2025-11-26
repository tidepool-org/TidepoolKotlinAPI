package org.tidepool.sdk.api

import org.tidepool.sdk.dto.summary.SummaryDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Path

interface SummaryApi {
    
    @GET("v1/summaries/{summaryType}/{userId}")
    suspend fun getSummary(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("summaryType") summaryType: String,
        @Path("userId") userId: String
    ): SummaryDto
}