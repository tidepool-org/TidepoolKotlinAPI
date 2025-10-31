package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import kotlin.time.Duration
import org.tidepool.sdk.model.data.ClientSoftware
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DeduplicatorDescriptor

@Serializable
data class DataSetDto(
    @SerialName("annotations")
    val annotations: List<Map<String, String>>? = null,
    @SerialName("byUser")
    val byUser: String? = null,
    @SerialName("client")
    val client: ClientSoftwareDto? = null,
    @SerialName("clockDriftOffset")
    @Contextual val clockDriftOffset: Duration? = null,
    @SerialName("computerTime")
    val computerTime: String? = null,
    @SerialName("conversionOffset")
    @Contextual val conversionOffset: Duration? = null,
    @SerialName("createdTime")
    @Contextual val createdTime: Instant? = null,
    @SerialName("createdUserId")
    val createdUserId: String? = null,
    @SerialName("dataSetType")
    val dataSetType: String? = null,
    @SerialName("deduplicator")
    val deduplicator: DeduplicatorDescriptorDto? = null,
    @SerialName("deletedTime")
    @Contextual val deletedTime: Instant? = null,
    @SerialName("deletedUserId")
    val deletedUserId: String? = null,
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
    @SerialName("id")
    val id: String? = null,
    @SerialName("modifiedTime")
    @Contextual val modifiedTime: Instant? = null,
    @SerialName("modifiedUserId")
    val modifiedUserId: String? = null,
    @SerialName("state")
    val state: String? = null,
    @SerialName("time")
    @Contextual val time: Instant? = null,
    @SerialName("timeProcessing")
    val timeProcessing: String? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("timezoneOffset")
    @Contextual val timezoneOffset: Duration? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("uploadId")
    val uploadId: String? = null,
    @SerialName("version")
    val version: String? = null
)

@Serializable
data class ClientSoftwareDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("version")
    val version: String? = null
)

@Serializable
data class DeduplicatorDescriptorDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("version")
    val version: String? = null
)

fun ClientSoftwareDto.toDomain(): ClientSoftware = ClientSoftware(
    name = name,
    version = version
)

fun ClientSoftware.toDto(): ClientSoftwareDto = ClientSoftwareDto(
    name = name,
    version = version
)

fun DataSetDto.toDomain(): DataSet = DataSet(
    annotations = annotations,
    byUser = byUser,
    client = client?.toDomain(),
    clockDriftOffset = clockDriftOffset,
    computerTime = computerTime,
    conversionOffset = conversionOffset,
    createdTime = createdTime,
    createdUserId = createdUserId,
    dataSetType = dataSetType,
    deduplicator = deduplicator?.toDomain(),
    deletedTime = deletedTime,
    deletedUserId = deletedUserId,
    deviceId = deviceId,
    deviceManufacturers = deviceManufacturers,
    deviceModel = deviceModel,
    deviceSerialNumber = deviceSerialNumber,
    deviceTags = deviceTags,
    id = id,
    modifiedTime = modifiedTime,
    modifiedUserId = modifiedUserId,
    state = state,
    time = time,
    timeProcessing = timeProcessing,
    timezone = timezone,
    timezoneOffset = timezoneOffset,
    type = type,
    uploadId = uploadId,
    version = version
)

fun DataSet.toDto(): DataSetDto = DataSetDto(
    annotations = annotations,
    byUser = byUser,
    client = client?.toDto(),
    clockDriftOffset = clockDriftOffset,
    computerTime = computerTime,
    conversionOffset = conversionOffset,
    createdTime = createdTime,
    createdUserId = createdUserId,
    dataSetType = dataSetType,
    deduplicator = deduplicator?.toDto(),
    deletedTime = deletedTime,
    deletedUserId = deletedUserId,
    deviceId = deviceId,
    deviceManufacturers = deviceManufacturers,
    deviceModel = deviceModel,
    deviceSerialNumber = deviceSerialNumber,
    deviceTags = deviceTags,
    id = id,
    modifiedTime = modifiedTime,
    modifiedUserId = modifiedUserId,
    state = state,
    time = time,
    timeProcessing = timeProcessing,
    timezone = timezone,
    timezoneOffset = timezoneOffset,
    type = type,
    uploadId = uploadId,
    version = version
)

fun DeduplicatorDescriptorDto.toDomain(): DeduplicatorDescriptor = DeduplicatorDescriptor(
    name = name,
    version = version,
)

fun DeduplicatorDescriptor.toDto(): DeduplicatorDescriptorDto = DeduplicatorDescriptorDto(
    name = name,
    version = version,
)