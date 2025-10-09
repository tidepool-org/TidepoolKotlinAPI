package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import kotlin.time.Duration

@Serializable
data class DataSetDto(
    val annotations: List<Map<String, String>>? = null,
    val byUser: String? = null,
    val client: ClientSoftwareDto? = null,
    @Contextual val clockDriftOffset: Duration? = null,
    val computerTime: String? = null,
    @Contextual val conversionOffset: Duration? = null,
    @Contextual val createdTime: Instant? = null,
    val createdUserId: String? = null,
    val dataSetType: String? = null,
    val deduplicator: DeduplicatorDescriptorDto? = null,
    @Contextual val deletedTime: Instant? = null,
    val deletedUserId: String? = null,
    val deviceId: String? = null,
    val deviceManufacturers: List<String>? = null,
    val deviceModel: String? = null,
    val deviceSerialNumber: String? = null,
    val deviceTags: List<String>? = null,
    val id: String? = null,
    @Contextual val modifiedTime: Instant? = null,
    val modifiedUserId: String? = null,
    val state: String? = null,
    @Contextual val time: Instant? = null,
    val timeProcessing: String? = null,
    val timezone: String? = null,
    @Contextual val timezoneOffset: Duration? = null,
    val type: String? = null,
    val uploadId: String? = null,
    val version: String? = null
)

@Serializable
data class ClientSoftwareDto(
    val name: String? = null,
    val version: String? = null
)

@Serializable
data class DeduplicatorDescriptorDto(
    val name: String? = null,
    val version: String? = null
)