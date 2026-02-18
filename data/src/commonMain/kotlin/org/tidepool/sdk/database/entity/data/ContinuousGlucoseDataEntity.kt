package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.data.ContinuousGlucoseDataDto
import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

@Entity(tableName = "continuous_glucose_data")
data class ContinuousGlucoseDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: String,
    @ColumnInfo(name = "type")
    override var type: String,
    @ColumnInfo(name = "time")
    override var time: Long? = null,
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
    override var timeZoneOffset: Int?, // Duration in minutes
    
    @ColumnInfo(name = "value")
    var value: Double? = null,
    @ColumnInfo(name = "units")
    var units: String? = null,
    @ColumnInfo(name = "trend")
    var trend: String? = null,
    @ColumnInfo(name = "trend_rate")
    var trendRate: Double? = null
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

fun ContinuousGlucoseDataDto.toEntity() = ContinuousGlucoseDataEntity(
    id = id,
    type = Json.encodeToString(type),
    time = time?.epochSeconds,
    annotations = annotations?.let { Json.encodeToString(it) },
    associations = associations.let { Json.encodeToString(it) },
    clockDriftOffset = clockDriftOffset?.inWholeMilliseconds,
    conversionOffset = conversionOffset?.inWholeMilliseconds,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes.let { Json.encodeToString(it) },
    timeZone = timeZone?.id,
    timeZoneOffset = timeZoneOffset,
    value = value,
    units = units?.let { Json.encodeToString(it) },
    trend = trend?.let { Json.encodeToString(it) },
    trendRate = trendRate,
)

fun ContinuousGlucoseDataEntity.toDto() = ContinuousGlucoseDataDto(
    id = id,
    type = Json.decodeFromString(type),
    time = time?.let { Instant.fromEpochSeconds(it) },
    annotations = annotations
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<List<Map<String, String>>>(it) },
    associations = associations
        ?.let { Json.decodeFromString<List<AssociationDto>>(it) }
        .orEmpty(),
    clockDriftOffset = clockDriftOffset?.milliseconds,
    conversionOffset = conversionOffset?.milliseconds,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes
        ?.let { Json.decodeFromString<List<String>>(it) }
        .orEmpty(),
    timeZone = timeZone?.let { TimeZone.getTimeZone(it) },
    timeZoneOffset = timeZoneOffset,
    value = value,
    units = units?.let { Json.decodeFromString(it) },
    trend = trend?.let { Json.decodeFromString(it) },
    trendRate = trendRate,
)
