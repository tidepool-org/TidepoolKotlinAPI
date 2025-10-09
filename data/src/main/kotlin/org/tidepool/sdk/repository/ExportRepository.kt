package org.tidepool.sdk.repository

import okhttp3.ResponseBody
import org.tidepool.sdk.dto.BloodGlucoseDto
import java.time.Instant

interface ExportRepository {
    
    suspend fun exportUserData(
        sessionToken: String,
        userId: String,
        startDate: Instant? = null,
        endDate: Instant? = null,
        bgUnits: BloodGlucoseDto.UnitsDto = BloodGlucoseDto.UnitsDto.MillimolesPerLiter,
        format: String,
    ): Result<ResponseBody>
}