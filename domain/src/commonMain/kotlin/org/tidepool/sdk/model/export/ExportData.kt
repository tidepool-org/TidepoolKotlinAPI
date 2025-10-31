package org.tidepool.sdk.model.export

import java.io.InputStream

data class ExportData(
    val data: InputStream,
    val format: ExportFormat,
    val contentType: String,
    val fileName: String? = null,
)