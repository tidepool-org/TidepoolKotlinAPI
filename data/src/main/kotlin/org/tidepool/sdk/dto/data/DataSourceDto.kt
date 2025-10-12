package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.DataSource
import java.time.Instant

@Serializable
@KonvertTo(DataSource::class, mapFunctionName = "toDomain")
data class DataSourceDto(
    @SerialName("createdTime")
    @Contextual val createdTime: Instant? = null,
    @SerialName("dataSetIds")
    val dataSetIds: List<String>? = null,
    @SerialName("earliestDataTime")
    @Contextual val earliestDataTime: Instant? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("lastImportTime")
    @Contextual val lastImportTime: Instant? = null,
    @SerialName("latestDataTime")
    @Contextual val latestDataTime: Instant? = null,
    @SerialName("modifiedTime")
    @Contextual val modifiedTime: Instant? = null,
    @SerialName("providerName")
    val providerName: String? = null,
    @SerialName("providerSessionId")
    val providerSessionId: String? = null,
    @SerialName("providerType")
    val providerType: String? = null,
    @SerialName("state")
    val state: String? = null
) {
    @KonvertFrom(DataSource::class, mapFunctionName = "fromDomain")
    companion object {}
}