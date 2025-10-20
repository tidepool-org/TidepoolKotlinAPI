package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import org.tidepool.sdk.dto.data.BasalAutomatedDataDto
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

@Entity(tableName = "basal_automated_data")
data class BasalAutomatedDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: String,
    @ColumnInfo(name = "type")
    override var type: String,
    @ColumnInfo(name = "time")
    override var time: Instant? = null,
    @ColumnInfo(name = "annotations")
    override var annotations: String? = null, // JSON string
    @ColumnInfo(name = "associations")
    override var associations: String? = null, // JSON string
    @ColumnInfo(name = "clock_drift_offset")
    override var clockDriftOffset: Long? = null, // Duration in milliseconds
    @ColumnInfo(name = "conversion_offset")
    override var conversionOffset: Long? = null, // Duration in milliseconds
    @ColumnInfo(name = "data_set_id")
    override var dataSetId: String? = null,
    @ColumnInfo(name = "device_time")
    override var deviceTime: String? = null,
    @ColumnInfo(name = "notes")
    override var notes: String? = null, // JSON string
    @ColumnInfo(name = "time_zone")
    override var timeZone: String? = null, // TimeZone ID
    @ColumnInfo(name = "time_zone_offset")
    override var timeZoneOffset: Long? = null, // Duration in milliseconds
    
    // BasalAutomatedDataDto specific fields
    @ColumnInfo(name = "delivery_type")
    var deliveryType: String,
    @ColumnInfo(name = "duration")
    var duration: Int,
    @ColumnInfo(name = "expected_duration")
    var expectedDuration: Int? = null,
    @ColumnInfo(name = "rate")
    var rate: Double = -1.0,
    @ColumnInfo(name = "schedule_name")
    var scheduleName: String? = null
) : BaseDataEntity(
    id = id,
    type = type,
    time = time,
    annotations = annotations,
    associations = associations,
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
)

fun BasalAutomatedDataDto.toEntity() = BasalAutomatedDataEntity(
    id = id,
    type = Json.encodeToString(type),
    time = time,
    annotations = Json.encodeToString(annotations.orEmpty()),
    associations = Json.encodeToString(associations.orEmpty()),
    clockDriftOffset = clockDriftOffset?.inWholeMilliseconds,
    conversionOffset = conversionOffset?.inWholeMilliseconds,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = Json.encodeToString(notes.orEmpty()),
    timeZone = timeZone?.id,
    timeZoneOffset = timeZoneOffset?.inWholeMilliseconds,
    deliveryType = Json.encodeToString(deliveryType),
    duration = duration,
    expectedDuration = expectedDuration,
    rate = rate,
    scheduleName = scheduleName,
)

fun BasalAutomatedDataEntity.toDto() = BasalAutomatedDataDto(
    id = id,
    type = Json.decodeFromString(type),
    time = time,
    annotations = if (annotations.isNullOrBlank()) emptyList() else Json.decodeFromString(annotations!!),
    associations = if (associations.isNullOrBlank()) emptyList() else Json.decodeFromString(associations!!),
    clockDriftOffset = clockDriftOffset?.milliseconds,
    conversionOffset = conversionOffset?.milliseconds,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = if (notes.isNullOrBlank()) emptyList() else Json.decodeFromString(notes!!),
    timeZone = timeZone?.let { TimeZone.getTimeZone(it) },
    timeZoneOffset = timeZoneOffset?.milliseconds,
    deliveryType = Json.decodeFromString(deliveryType),
    duration = duration,
    expectedDuration = expectedDuration,
    rate = rate,
    scheduleName = scheduleName,
)