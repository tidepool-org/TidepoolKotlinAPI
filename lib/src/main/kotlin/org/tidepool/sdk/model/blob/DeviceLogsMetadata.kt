package org.tidepool.sdk.model.blob

import org.tidepool.sdk.dto.blob.DeviceLogContentDto
import org.tidepool.sdk.dto.blob.DeviceLogTypeDto
import org.tidepool.sdk.dto.blob.DeviceLogsMetadataDto
import java.time.Instant

data class DeviceLogsMetadata(
    val id: String,
    val userId: String,
    val digestMD5: String,
    val mediaType: String,
    val size: Long,
    val createdTime: Instant,
    val startAtTime: Instant,
    val endAtTime: Instant
)

data class DeviceLogContent(
    val type: DeviceLogType,
    val managerIdentifier: String,
    val deviceIdentifier: String,
    val timestamp: Instant,
    val message: String
)

enum class DeviceLogType {
    Send,
    Receive,
    Error,
    Delegate,
    DelegateResponse,
    Connection
}

// Conversion functions
internal fun DeviceLogsMetadataDto.toDomain() = DeviceLogsMetadata(
    id = id,
    userId = userId,
    digestMD5 = digestMD5,
    mediaType = mediaType,
    size = size,
    createdTime = createdTime,
    startAtTime = startAtTime,
    endAtTime = endAtTime
)

internal fun DeviceLogContentDto.toDomain() = DeviceLogContent(
    type = type.toDomain(),
    managerIdentifier = managerIdentifier,
    deviceIdentifier = deviceIdentifier,
    timestamp = timestamp,
    message = message
)

internal fun DeviceLogTypeDto.toDomain() = when (this) {
    DeviceLogTypeDto.Send             -> DeviceLogType.Send
    DeviceLogTypeDto.Receive          -> DeviceLogType.Receive
    DeviceLogTypeDto.Error            -> DeviceLogType.Error
    DeviceLogTypeDto.Delegate         -> DeviceLogType.Delegate
    DeviceLogTypeDto.DelegateResponse -> DeviceLogType.DelegateResponse
    DeviceLogTypeDto.Connection       -> DeviceLogType.Connection
}

internal fun DeviceLogType.toDto() = when (this) {
    DeviceLogType.Send             -> DeviceLogTypeDto.Send
    DeviceLogType.Receive          -> DeviceLogTypeDto.Receive
    DeviceLogType.Error            -> DeviceLogTypeDto.Error
    DeviceLogType.Delegate         -> DeviceLogTypeDto.Delegate
    DeviceLogType.DelegateResponse -> DeviceLogTypeDto.DelegateResponse
    DeviceLogType.Connection       -> DeviceLogTypeDto.Connection
}

internal fun DeviceLogContent.toDto() = DeviceLogContentDto(
    type = type.toDto(),
    managerIdentifier = managerIdentifier,
    deviceIdentifier = deviceIdentifier,
    timestamp = timestamp,
    message = message
)