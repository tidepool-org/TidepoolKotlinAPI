package org.tidepool.sdk.dto.blob

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class DeviceLogsMetadataDto(
    val id: String,
    val userId: String,
    val digestMD5: String,
    val mediaType: String,
    val size: Long,
    @Contextual val createdTime: Instant,
    @Contextual val startAtTime: Instant,
    @Contextual val endAtTime: Instant
)

@Serializable
data class DeviceLogContentDto(
    val type: DeviceLogTypeDto,
    val managerIdentifier: String,
    val deviceIdentifier: String,
    @Contextual val timestamp: Instant,
    val message: String
)

@Serializable
enum class DeviceLogTypeDto {
    
    @kotlinx.serialization.SerialName("send")
    Send,
    
    @kotlinx.serialization.SerialName("receive")
    Receive,
    
    @kotlinx.serialization.SerialName("error")
    Error,
    
    @kotlinx.serialization.SerialName("delegate")
    Delegate,
    
    @kotlinx.serialization.SerialName("delegateResponse")
    DelegateResponse,
    
    @kotlinx.serialization.SerialName("connection")
    Connection
}