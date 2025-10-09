package org.tidepool.sdk.repository.impl

import okhttp3.ResponseBody
import org.tidepool.sdk.api.ExportApi
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.repository.ExportRepository
import org.tidepool.sdk.runCatchingNetworkExceptions
import java.time.Instant

class ExportRepositoryImpl(
    private val exportApi: ExportApi,
) : ExportRepository {
    
    override suspend fun exportUserData(
        sessionToken: String,
        userId: String,
        startDate: Instant?,
        endDate: Instant?,
        bgUnits: BloodGlucoseDto.UnitsDto,
        format: String,
    ): Result<ResponseBody> = runCatchingNetworkExceptions {
        exportApi.exportUserData(
            sessionToken = sessionToken,
            userId = userId,
            startDate = startDate,
            endDate = endDate,
            format = format,
            bgUnits = bgUnits.shorthand,
        )
    }
}