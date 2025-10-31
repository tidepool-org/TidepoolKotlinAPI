package org.tidepool.sdk.repository

import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.export.ExportData
import org.tidepool.sdk.model.export.ExportFormat
import java.time.Instant

interface ExportRepository {
    
//    suspend fun exportUserData(
//        sessionToken: String,
//        userId: String,
//        startDate: Instant? = null,
//        endDate: Instant? = null,
//        bgUnits: BloodGlucose.Units = BloodGlucose.Units.MillimolesPerLiter,
//        format: ExportFormat,
//    ): Result<ExportData>
}