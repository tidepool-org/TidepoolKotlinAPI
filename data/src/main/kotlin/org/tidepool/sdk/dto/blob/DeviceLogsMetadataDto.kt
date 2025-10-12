package org.tidepool.sdk.dto.blob

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.blob.DeviceLogContent
import org.tidepool.sdk.model.blob.DeviceLogType
import java.time.Instant

@Serializable
@KonvertTo(org.tidepool.sdk.model.blob.DeviceLogsMetadata::class, mapFunctionName = "toDomain")
data class DeviceLogsMetadataDto(
    @SerialName("id")
    val id: String,
    @SerialName("userId")
    val userId: String,
    @SerialName("digestMD5")
    val digestMD5: String,
    @SerialName("mediaType")
    val mediaType: String,
    @SerialName("size")
    val size: Long,
    @Contextual
    @SerialName("createdTime")
    val createdTime: Instant,
    @Contextual
    @SerialName("startAtTime")
    val startAtTime: Instant,
    @Contextual
    @SerialName("endAtTime")
    val endAtTime: Instant
)

@Serializable
@KonvertTo(DeviceLogContent::class, mapFunctionName = "toDomain")
data class DeviceLogContentDto(
    @SerialName("type")
    val type: DeviceLogTypeDto,
    @SerialName("managerIdentifier")
    val managerIdentifier: String,
    @SerialName("deviceIdentifier")
    val deviceIdentifier: String,
    @Contextual
    @SerialName("timestamp")
    val timestamp: Instant,
    @SerialName("message")
    val message: String
)

fun DeviceLogContent.toDto() = DeviceLogContentDto(
    type = type.toDto(),
    managerIdentifier = managerIdentifier,
    deviceIdentifier = deviceIdentifier,
    timestamp = timestamp,
    message = message
)

@Serializable
enum class DeviceLogTypeDto {
    
    @SerialName("send")
    Send,
    
    @SerialName("receive")
    Receive,
    
    @SerialName("error")
    Error,
    
    @SerialName("delegate")
    Delegate,
    
    @SerialName("delegateResponse")
    DelegateResponse,
    
    @SerialName("connection")
    Connection
}

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