package org.tidepool.sdk.repository

import org.tidepool.sdk.api.ExportApi
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.export.ExportData
import org.tidepool.sdk.model.export.ExportFormat
import org.tidepool.sdk.runCatchingNetworkExceptions
import java.time.Instant
import kotlin.toString

class ExportRepositoryImpl(
    private val exportApi: ExportApi,
) : ExportRepository {
    
//    override suspend fun exportUserData(
//        sessionToken: String,
//        userId: String,
//        startDate: Instant?,
//        endDate: Instant?,
//        bgUnits: BloodGlucose.Units,
//        format: ExportFormat,
//    ): Result<ExportData> = runCatchingNetworkExceptions {
//        exportApi.exportUserData(
//            sessionToken = sessionToken,
//            userId = userId,
//            startDate = startDate,
//            endDate = endDate,
//            format = format.fileType,
//            bgUnits = bgUnits.shorthand,
//        )
//    }.map { it.toExportData(format) }
}

//internal fun ResponseBody.toExportData(format: ExportFormat): ExportData {
//    val contentType = this.contentType()?.toString() ?: format.contentType
//    val fileName = "TidepoolExport." + format.fileType
//
//    return ExportData(
//        data = this.byteStream(),
//        format = format,
//        contentType = contentType,
//        fileName = fileName
//    )
//}