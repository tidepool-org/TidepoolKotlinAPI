package org.tidepool.sdk.model.blob

import kotlinx.datetime.Instant

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