package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.data.DeviceEventDataDto
import org.tidepool.sdk.dto.data.OriginDto
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

@Entity(tableName = "device_event_data")
data class DeviceEventDataEntity(
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
    var subType: String,
    @ColumnInfo(name = "device_identifier")
    var deviceIdentifier: String? = null,
    @ColumnInfo(name = "expected_lifetime_seconds")
    var expectedLifetimeSeconds: Double? = null,
    @ColumnInfo(name = "warmup_period_seconds")
    var warmupPeriodSeconds: Double? = null,
    @ColumnInfo(name = "failure_message")
    var failureMessage: String? = null,
    @ColumnInfo(name = "origin")
    var origin: String? = null, // JSON string
    @ColumnInfo(name = "payload")
    var payload: String? = null, // JSON string
    @ColumnInfo(name = "status")
    var status: String? = null, // JSON string
    @ColumnInfo(name = "reason")
    var reason: String? = null, // JSON string
    @ColumnInfo(name = "override_type")
    var overrideType: String? = null, // JSON string
    @ColumnInfo(name = "override_preset")
    var overridePreset: String? = null,
    @ColumnInfo(name = "method")
    var method: String? = null, // JSON string
    @ColumnInfo(name = "duration")
    var duration: Long? = null, // milliseconds
    @ColumnInfo(name = "bg_target")
    var bgTarget: String? = null, // JSON string
    @ColumnInfo(name = "units")
    var units: String? = null, // JSON string
    @ColumnInfo(name = "basal_rate_scale_factor")
    var basalRateScaleFactor: Double? = null,
    @ColumnInfo(name = "carb_ratio_scale_factor")
    var carbRatioScaleFactor: Double? = null,
    @ColumnInfo(name = "insulin_sensitivity_scale_factor")
    var insulinSensitivityScaleFactor: Double? = null,
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

fun DeviceEventDataDto.toEntity() = DeviceEventDataEntity(
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
    subType = Json.encodeToString(subType),
    deviceIdentifier = deviceIdentifier,
    expectedLifetimeSeconds = expectedLifetimeSeconds,
    warmupPeriodSeconds = warmupPeriodSeconds,
    failureMessage = failureMessage,
    origin = origin?.let { Json.encodeToString(it) },
    payload = payload?.let { Json.encodeToString(it) },
    status = status?.let { Json.encodeToString(it) },
    reason = reason?.let { Json.encodeToString(it) },
    overrideType = overrideType?.let { Json.encodeToString(it) },
    overridePreset = overridePreset,
    method = method?.let { Json.encodeToString(it) },
    duration = duration,
    bgTarget = bgTarget?.let { Json.encodeToString(it) },
    units = units?.let { Json.encodeToString(it) },
    basalRateScaleFactor = basalRateScaleFactor,
    carbRatioScaleFactor = carbRatioScaleFactor,
    insulinSensitivityScaleFactor = insulinSensitivityScaleFactor,
)

fun DeviceEventDataEntity.toDto() = DeviceEventDataDto(
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
    subType = Json.decodeFromString(subType),
    deviceIdentifier = deviceIdentifier,
    expectedLifetimeSeconds = expectedLifetimeSeconds,
    warmupPeriodSeconds = warmupPeriodSeconds,
    failureMessage = failureMessage,
    origin = origin?.takeUnless { it == "null" }?.let { Json.decodeFromString<OriginDto>(it) },
    payload = payload?.takeUnless { it == "null" }?.let { Json.decodeFromString<DeviceEventDataDto.PayloadDto>(it) },
    status = status?.let { Json.decodeFromString<DeviceEventDataDto.StatusDto>(it) },
    reason = reason?.let {
        Json.decodeFromString<Map<DeviceEventDataDto.StatusDto, DeviceEventDataDto.StatusReasonDto>>(it)
    },
    overrideType = overrideType?.let { Json.decodeFromString<DeviceEventDataDto.OverrideTypeDto>(it) },
    overridePreset = overridePreset,
    method = method?.let { Json.decodeFromString<DeviceEventDataDto.MethodDto>(it) },
    duration = duration,
    bgTarget = bgTarget?.let { Json.decodeFromString<BloodGlucoseDto.TargetDto>(it) },
    units = units?.let { Json.decodeFromString<DeviceEventDataDto.UnitsDto>(it) },
    basalRateScaleFactor = basalRateScaleFactor,
    carbRatioScaleFactor = carbRatioScaleFactor,
    insulinSensitivityScaleFactor = insulinSensitivityScaleFactor,
)
