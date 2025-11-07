package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.data.BolusDataDto
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

@Entity(tableName = "bolus_data")
data class BolusDataEntity(
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

    @ColumnInfo(name = "sub_type")
    var subType: String = "normal",
    @ColumnInfo(name = "delivery_context")
    var deliveryContext: String = "",
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

fun BolusDataDto.toEntity() = BolusDataEntity(
    id = id,
    type = Json.encodeToString(type),
    time = time?.epochSecond,
    annotations = Json.encodeToString(annotations),
    associations = Json.encodeToString(associations),
    clockDriftOffset = clockDriftOffset?.inWholeMilliseconds,
    conversionOffset = conversionOffset?.inWholeMilliseconds,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = Json.encodeToString(notes),
    timeZone = timeZone?.id,
    timeZoneOffset = timeZoneOffset,
    subType = Json.encodeToString(subType),
    deliveryContext = Json.encodeToString(deliveryContext),
)

fun BolusDataEntity.toDto() = BolusDataDto(
    id = id,
    type = Json.decodeFromString(type),
    time = time?.let { Instant.ofEpochSecond(it) },
    annotations = annotations
        ?.let { Json.decodeFromString<List<Map<String, String>>>(it) }
        .orEmpty(),
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
    subType = Json.decodeFromString(subType),
    deliveryContext = Json.decodeFromString(deliveryContext),
)
