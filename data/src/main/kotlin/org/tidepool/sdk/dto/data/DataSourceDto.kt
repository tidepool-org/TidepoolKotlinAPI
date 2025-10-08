package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class DataSourceDto(
    @Contextual val createdTime: Instant? = null,
    val dataSetIds: List<String>? = null,
    @Contextual val earliestDataTime: Instant? = null,
    val id: String? = null,
    @Contextual val lastImportTime: Instant? = null,
    @Contextual val latestDataTime: Instant? = null,
    @Contextual val modifiedTime: Instant? = null,
    val providerName: String? = null,
    val providerSessionId: String? = null,
    val providerType: String? = null,
    val state: String? = null
)