package org.tidepool.sdk.api

import org.tidepool.sdk.dto.general.MinimumClientVersionsDto
import de.jensklingenberg.ktorfit.http.GET

interface GeneralApi {
    
    @GET("info")
    suspend fun getMinimumClientVersions(): MinimumClientVersionsDto
}