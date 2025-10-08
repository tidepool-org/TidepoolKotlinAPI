package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.ClientSoftwareDto
import org.tidepool.sdk.dto.data.DataSetDto
import org.tidepool.sdk.dto.data.DeduplicatorDescriptorDto
import org.tidepool.sdk.dto.data.NewDataSetDto

// DataSet mappers
internal fun DataSetDto.toDomain(): DataSet = DataSet(
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

internal fun DataSet.toDto(): DataSetDto = DataSetDto(
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

// NewDataSet mappers
fun NewDataSetDto.toDomain(): NewDataSet = NewDataSet(
    client = client?.toDomain(),
    dataSetType = dataSetType,
    deviceId = deviceId,
    deviceManufacturers = deviceManufacturers,
    deviceModel = deviceModel,
    deviceSerialNumber = deviceSerialNumber,
    deviceTags = deviceTags,
    deduplicator = deduplicator?.toDomain(),
    time = time,
    timeProcessing = timeProcessing,
    timezone = timezone,
    timezoneOffset = timezoneOffset
)

fun NewDataSet.toDto(): NewDataSetDto = NewDataSetDto(
    client = client?.toDto(),
    dataSetType = dataSetType,
    deviceId = deviceId,
    deviceManufacturers = deviceManufacturers,
    deviceModel = deviceModel,
    deviceSerialNumber = deviceSerialNumber,
    deviceTags = deviceTags,
    deduplicator = deduplicator?.toDto(),
    time = time,
    timeProcessing = timeProcessing,
    timezone = timezone,
    timezoneOffset = timezoneOffset
)

// ClientSoftware mappers
fun ClientSoftwareDto.toDomain(): ClientSoftware = ClientSoftware(
    name = name,
    version = version
)

fun ClientSoftware.toDto(): ClientSoftwareDto = ClientSoftwareDto(
    name = name,
    version = version
)

// DeduplicatorDescriptor mappers
fun DeduplicatorDescriptorDto.toDomain(): DeduplicatorDescriptor = DeduplicatorDescriptor(
    name = name,
    version = version
)

fun DeduplicatorDescriptor.toDto(): DeduplicatorDescriptorDto = DeduplicatorDescriptorDto(
    name = name,
    version = version
)