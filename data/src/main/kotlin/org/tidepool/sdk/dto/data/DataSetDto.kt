package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.DosingDecisionData
import java.time.Instant
import kotlin.time.Duration
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.ClientSoftware
import org.tidepool.sdk.model.data.DeduplicatorDescriptor

@Serializable
@KonvertTo(DataSet::class, mapFunctionName = "toDomain")
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
) {
    @KonvertFrom(DataSet::class, mapFunctionName = "fromDomain")
    companion object {}
}

@Serializable
@KonvertTo(ClientSoftware::class, mapFunctionName = "toDomain")
data class ClientSoftwareDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("version")
    val version: String? = null
) {
    @KonvertFrom(ClientSoftware::class, mapFunctionName = "fromDomain")
    companion object {}
}

@Serializable
@KonvertTo(DeduplicatorDescriptor::class, mapFunctionName = "toDomain")
data class DeduplicatorDescriptorDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("version")
    val version: String? = null
) {
    @KonvertFrom(DeduplicatorDescriptor::class, mapFunctionName = "fromDomain")
    companion object {}
}