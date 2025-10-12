package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.DosingDecisionData
import org.tidepool.sdk.model.data.NewDataSet
import java.time.Instant
import kotlin.time.Duration

@Serializable
@KonvertTo(NewDataSet::class, mapFunctionName = "toDomain")
data class NewDataSetDto(
    @SerialName("client")
    val client: ClientSoftwareDto? = null,
    @SerialName("dataSetType")
    val dataSetType: String? = null,
    @SerialName("deviceId")
    val deviceId: String? = null,
    @SerialName("deviceManufacturers")
    val deviceManufacturers: List<String>? = null,
    @SerialName("deviceModel")
    val deviceModel: String? = null,
    @SerialName("deviceSerialNumber")
    val deviceSerialNumber: String? = null,
    @SerialName("deviceTags")
    val deviceTags: List<String>? = null,
    @SerialName("deduplicator")
    val deduplicator: DeduplicatorDescriptorDto? = null,
    @SerialName("time")
    @Contextual val time: Instant? = null,
    @SerialName("timeProcessing")
    val timeProcessing: String? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("timezoneOffset")
    @Contextual val timezoneOffset: Duration? = null
) {
    @KonvertFrom(NewDataSet::class, mapFunctionName = "fromDomain")
    companion object {}
}