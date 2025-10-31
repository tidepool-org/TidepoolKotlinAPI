package org.tidepool.sdk.model.data

import java.time.Instant

data class DataSource(
    val createdTime: Instant? = null,
    val dataSetIds: List<String>? = null,
    val earliestDataTime: Instant? = null,
    val id: String? = null,
    val lastImportTime: Instant? = null,
    val latestDataTime: Instant? = null,
    val modifiedTime: Instant? = null,
    val providerName: String? = null,
    val providerSessionId: String? = null,
    val providerType: String? = null,
    val state: String? = null
)

data class NewDataSource(
    val providerName: String? = null,
    val providerSessionId: String? = null,
    val providerType: String? = null
)