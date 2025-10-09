package org.tidepool.sdk.model.export

import okhttp3.ResponseBody
import java.io.InputStream

data class ExportData(
    val data: InputStream,
    val format: ExportFormat,
    val contentType: String,
    val fileName: String? = null,
)

internal fun ResponseBody.toExportData(format: ExportFormat): ExportData {
    val contentType = this.contentType()?.toString() ?: format.contentType
    val fileName = "TidepoolExport." + format.fileType
    
    return ExportData(
        data = this.byteStream(),
        format = format,
        contentType = contentType,
        fileName = fileName
    )
}