package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.data.CgmSettingsDataDto
import org.tidepool.sdk.dto.data.OriginDto
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

// Table dropped and recreated with these columns in the outbox's v3->v4 migration: it had never
// been drained, so there was nothing to lose.
@Entity(tableName = "cgm_settings_data")
data class CgmSettingsDataEntity(
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

    @ColumnInfo(name = "firmware_version")
    var firmwareVersion: String? = null,
    @ColumnInfo(name = "hardware_version")
    var hardwareVersion: String? = null,
    @ColumnInfo(name = "manufacturers")
    var manufacturers: String? = null, // JSON string
    @ColumnInfo(name = "model")
    var model: String? = null,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "software_version")
    var softwareVersion: String? = null,
    @ColumnInfo(name = "transmitter_id")
    var transmitterId: String? = null,
    @ColumnInfo(name = "units")
    var units: String? = null, // JSON string
    @ColumnInfo(name = "origin")
    var origin: String? = null, // JSON string
    @ColumnInfo(name = "payload")
    var payload: String? = null, // JSON string
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

fun CgmSettingsDataDto.toEntity() = CgmSettingsDataEntity(
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
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    manufacturers = manufacturers?.let { Json.encodeToString(it) },
    model = model,
    name = name,
    softwareVersion = softwareVersion,
    transmitterId = transmitterId,
    units = units?.let { Json.encodeToString(it) },
    origin = origin?.let { Json.encodeToString(it) },
    payload = payload?.let { Json.encodeToString(it) },
)

fun CgmSettingsDataEntity.toDto() = CgmSettingsDataDto(
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
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    manufacturers = manufacturers?.takeUnless { it == "null" }?.let { Json.decodeFromString(it) },
    model = model,
    name = name,
    softwareVersion = softwareVersion,
    transmitterId = transmitterId,
    units = units?.takeUnless { it == "null" }?.let { Json.decodeFromString(it) },
    origin = origin?.takeUnless { it == "null" }?.let { Json.decodeFromString<OriginDto>(it) },
    payload = payload
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<CgmSettingsDataDto.PayloadDto>(it) },
)
