package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.NewDataSet
import kotlinx.datetime.Instant
import kotlin.time.Duration

@Serializable
data class NewDataSetDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("type")
    val type: String? = "upload",
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
    val deviceTags: List<DeviceTagDto>? = null,
    @SerialName("deduplicator")
    val deduplicator: DeduplicatorDescriptorDto? = null,
    @SerialName("time")
    @Contextual val time: Instant? = null,
    @SerialName("timeProcessing")
    val timeProcessing: String? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("timezoneOffset")
    val timeZoneOffset: Int? = null,
)

fun NewDataSet.toDto(): NewDataSetDto = NewDataSetDto(
    client = client?.toDto(),
    dataSetType = dataSetType,
    deviceId = deviceId,
    deviceManufacturers = deviceManufacturers,
    deviceModel = deviceModel,
    deviceSerialNumber = deviceSerialNumber,
    deviceTags = deviceTags?.map { it.toDto() },
    deduplicator = deduplicator?.toDto(),
    time = time,
    timeProcessing = timeProcessing,
    timezone = timezone,
    timeZoneOffset = timeZoneOffset,
)