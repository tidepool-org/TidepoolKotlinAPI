package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.export.ExportData
import org.tidepool.sdk.model.export.ExportFormat
import org.tidepool.sdk.repository.ExportRepository
import org.tidepool.sdk.repository.UserRepository
import java.time.Instant

class ExportService internal constructor(
    private val exportRepository: ExportRepository,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) {
    
//    suspend fun exportUserData(
//        startDate: Instant? = null,
//        endDate: Instant? = null,
//        format: ExportFormat = ExportFormat.Xlsx,
//        bgUnits: BloodGlucose.Units = BloodGlucose.Units.MillimolesPerLiter,
//    ): Result<ExportData> = tokenProvider.getToken().flatMap { token ->
//        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
//            exportRepository.exportUserData(
//                sessionToken = token,
//                userId = user.userId,
//                startDate = startDate,
//                endDate = endDate,
//                bgUnits = bgUnits,
//                format = format,
//            )
//        }
//    }
}