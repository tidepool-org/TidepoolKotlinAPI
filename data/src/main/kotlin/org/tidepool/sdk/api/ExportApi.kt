package org.tidepool.sdk.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Instant

interface ExportApi {
    
    @GET("/export/{userId}")
    suspend fun exportUserData(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("startDate") startDate: Instant? = null,
        @Query("endDate") endDate: Instant? = null,
        @Query("format") format: String = "xlsx",
        @Query("bgUnits") bgUnits: String = "mmol/L"
    ): ResponseBody
}