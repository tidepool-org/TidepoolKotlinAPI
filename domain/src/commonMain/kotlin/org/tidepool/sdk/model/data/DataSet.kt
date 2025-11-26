package org.tidepool.sdk.model.data

import kotlinx.datetime.Instant
import kotlin.time.Duration

data class DataSet(
    val annotations: List<Map<String, String>>? = null,
    val byUser: String? = null,
    val client: ClientSoftware? = null,
    val clockDriftOffset: Duration? = null,
    val computerTime: String? = null,
    val conversionOffset: Duration? = null,
    val createdTime: Instant? = null,
    val createdUserId: String? = null,
    val dataSetType: String? = null,
    val deduplicator: DeduplicatorDescriptor? = null,
    val deletedTime: Instant? = null,
    val deletedUserId: String? = null,
    val deviceId: String? = null,
    val deviceManufacturers: List<String>? = null,
    val deviceModel: String? = null,
    val deviceSerialNumber: String? = null,
    val deviceTags: List<DeviceTag>? = null,
    val id: String? = null,
    val modifiedTime: Instant? = null,
    val modifiedUserId: String? = null,
    val state: String? = null,
    val time: Instant? = null,
    val timeProcessing: String? = null,
    val timezone: String? = null,
    val timeZoneOffset: Int? = null,
    val type: String? = null,
    val uploadId: String? = null,
    val version: String? = null
)

data class ClientSoftware(
    val name: String? = null,
    val version: String? = null
)

data class DeduplicatorDescriptor(
    val name: String? = null,
    val version: String? = null
)

data class NewDataSet(
    val client: ClientSoftware? = null,
    val dataSetType: String? = null,
    val deviceId: String? = null,
    val deviceManufacturers: List<String>? = null,
    val deviceModel: String? = null,
    val deviceSerialNumber: String? = null,
    val deviceTags: List<DeviceTag>? = null,
    val deduplicator: DeduplicatorDescriptor? = null,
    val time: Instant? = null,
    val timeProcessing: String? = null,
    val timezone: String? = null,
    val timeZoneOffset: Int? = null
)

enum class DeviceTag {
    Bgm,
    Cgm,
    InsulinPump,
}