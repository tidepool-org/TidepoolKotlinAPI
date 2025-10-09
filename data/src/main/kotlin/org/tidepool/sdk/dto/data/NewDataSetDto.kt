package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import kotlin.time.Duration

@Serializable
data class NewDataSetDto(
    val client: ClientSoftwareDto? = null,
    val dataSetType: String? = null,
    val deviceId: String? = null,
    val deviceManufacturers: List<String>? = null,
    val deviceModel: String? = null,
    val deviceSerialNumber: String? = null,
    val deviceTags: List<String>? = null,
    val deduplicator: DeduplicatorDescriptorDto? = null,
    @Contextual val time: Instant? = null,
    val timeProcessing: String? = null,
    val timezone: String? = null,
    @Contextual val timezoneOffset: Duration? = null
)