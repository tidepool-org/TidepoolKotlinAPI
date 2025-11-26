package org.tidepool.sdk.api

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.datetime.Instant

interface ExportApi {
    
//    @GET("export/{userId}")
//    suspend fun exportUserData(
//        @Header("X-Tidepool-Session-Token") sessionToken: String,
//        @Path("userId") userId: String,
//        @Query("startDate") startDate: Instant? = null,
//        @Query("endDate") endDate: Instant? = null,
//        @Query("format") format: String = "xlsx",
//        @Query("bgUnits") bgUnits: String = "mmol/L"
//    ): ResponseBody
}