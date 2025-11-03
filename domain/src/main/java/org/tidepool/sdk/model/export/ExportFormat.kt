package org.tidepool.sdk.model.export

enum class ExportFormat(
    val fileType: String,
    val contentType: String,
) {
    Json(
        "json",
        "application/json",
    ),
    Xlsx(
        "xlsx",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    ),
    ;
}