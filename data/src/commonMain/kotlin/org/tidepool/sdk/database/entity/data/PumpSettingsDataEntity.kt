package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.data.OriginDto
import org.tidepool.sdk.dto.data.PumpSettingsDataDto
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

// Table dropped and recreated with these columns in the outbox's v3->v4 migration: it had never
// been drained, so there was nothing to lose. Nested structures (schedules, basal/bolus limits,
// display, insulin model, override presets, units) are stored as JSON text columns, as origin
// already is elsewhere.
@Entity(tableName = "pump_settings_data")
data class PumpSettingsDataEntity(
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

    @ColumnInfo(name = "active_schedule_name")
    var activeScheduleName: String? = null,
    @ColumnInfo(name = "basal")
    var basal: String? = null, // JSON string
    @ColumnInfo(name = "basal_rate_schedules")
    var basalRateSchedules: String? = null, // JSON string
    @ColumnInfo(name = "blood_glucose_safety_limit")
    var bloodGlucoseSafetyLimit: Double? = null,
    @ColumnInfo(name = "blood_glucose_target_physical_activity")
    var bloodGlucoseTargetPhysicalActivity: String? = null, // JSON string
    @ColumnInfo(name = "blood_glucose_target_preprandial")
    var bloodGlucoseTargetPreprandial: String? = null, // JSON string
    @ColumnInfo(name = "blood_glucose_target_schedules")
    var bloodGlucoseTargetSchedules: String? = null, // JSON string
    @ColumnInfo(name = "bolus")
    var bolus: String? = null, // JSON string
    @ColumnInfo(name = "carbohydrate_ratio_schedules")
    var carbohydrateRatioSchedules: String? = null, // JSON string
    @ColumnInfo(name = "display")
    var display: String? = null, // JSON string
    @ColumnInfo(name = "firmware_version")
    var firmwareVersion: String? = null,
    @ColumnInfo(name = "hardware_version")
    var hardwareVersion: String? = null,
    @ColumnInfo(name = "insulin_formulation")
    var insulinFormulation: String? = null, // JSON string
    @ColumnInfo(name = "insulin_model")
    var insulinModel: String? = null, // JSON string
    @ColumnInfo(name = "insulin_sensitivity_schedules")
    var insulinSensitivitySchedules: String? = null, // JSON string
    @ColumnInfo(name = "manufacturers")
    var manufacturers: String? = null, // JSON string
    @ColumnInfo(name = "model")
    var model: String? = null,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "override_presets")
    var overridePresets: String? = null, // JSON string
    @ColumnInfo(name = "schedule_time_zone_offset")
    var scheduleTimeZoneOffset: Int? = null,
    @ColumnInfo(name = "software_version")
    var softwareVersion: String? = null,
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

fun PumpSettingsDataDto.toEntity() = PumpSettingsDataEntity(
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
    activeScheduleName = activeScheduleName,
    basal = basal?.let { Json.encodeToString(it) },
    basalRateSchedules = basalRateSchedules?.let { Json.encodeToString(it) },
    bloodGlucoseSafetyLimit = bloodGlucoseSafetyLimit,
    bloodGlucoseTargetPhysicalActivity = bloodGlucoseTargetPhysicalActivity?.let { Json.encodeToString(it) },
    bloodGlucoseTargetPreprandial = bloodGlucoseTargetPreprandial?.let { Json.encodeToString(it) },
    bloodGlucoseTargetSchedules = bloodGlucoseTargetSchedules?.let { Json.encodeToString(it) },
    bolus = bolus?.let { Json.encodeToString(it) },
    carbohydrateRatioSchedules = carbohydrateRatioSchedules?.let { Json.encodeToString(it) },
    display = display?.let { Json.encodeToString(it) },
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    insulinFormulation = insulinFormulation?.let { Json.encodeToString(it) },
    insulinModel = insulinModel?.let { Json.encodeToString(it) },
    insulinSensitivitySchedules = insulinSensitivitySchedules?.let { Json.encodeToString(it) },
    manufacturers = manufacturers?.let { Json.encodeToString(it) },
    model = model,
    name = name,
    overridePresets = overridePresets?.let { Json.encodeToString(it) },
    scheduleTimeZoneOffset = scheduleTimeZoneOffset,
    softwareVersion = softwareVersion,
    units = units?.let { Json.encodeToString(it) },
    origin = origin?.let { Json.encodeToString(it) },
    payload = payload?.let { Json.encodeToString(it) },
)

fun PumpSettingsDataEntity.toDto() = PumpSettingsDataDto(
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
    activeScheduleName = activeScheduleName,
    basal = basal?.takeUnless { it == "null" }?.let { Json.decodeFromString<PumpSettingsDataDto.BasalDto>(it) },
    basalRateSchedules = basalRateSchedules
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<Map<String, List<PumpSettingsDataDto.ScheduleStartDto>>>(it) },
    bloodGlucoseSafetyLimit = bloodGlucoseSafetyLimit,
    bloodGlucoseTargetPhysicalActivity = bloodGlucoseTargetPhysicalActivity
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<BloodGlucoseDto.TargetDto>(it) },
    bloodGlucoseTargetPreprandial = bloodGlucoseTargetPreprandial
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<BloodGlucoseDto.TargetDto>(it) },
    bloodGlucoseTargetSchedules = bloodGlucoseTargetSchedules
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<Map<String, List<BloodGlucoseDto.StartTargetDto>>>(it) },
    bolus = bolus?.takeUnless { it == "null" }?.let { Json.decodeFromString<PumpSettingsDataDto.BolusDto>(it) },
    carbohydrateRatioSchedules = carbohydrateRatioSchedules
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<Map<String, List<PumpSettingsDataDto.ScheduleStartDto>>>(it) },
    display = display?.takeUnless { it == "null" }?.let { Json.decodeFromString<PumpSettingsDataDto.DisplayDto>(it) },
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    insulinFormulation = insulinFormulation
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<PumpSettingsDataDto.InsulinFormulationDto>(it) },
    insulinModel = insulinModel
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<PumpSettingsDataDto.InsulinModelDto>(it) },
    insulinSensitivitySchedules = insulinSensitivitySchedules
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<Map<String, List<PumpSettingsDataDto.ScheduleStartDto>>>(it) },
    manufacturers = manufacturers?.takeUnless { it == "null" }?.let { Json.decodeFromString(it) },
    model = model,
    name = name,
    overridePresets = overridePresets
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<Map<String, PumpSettingsDataDto.OverridePresetDto>>(it) },
    scheduleTimeZoneOffset = scheduleTimeZoneOffset,
    softwareVersion = softwareVersion,
    units = units?.takeUnless { it == "null" }?.let { Json.decodeFromString<PumpSettingsDataDto.UnitsDto>(it) },
    origin = origin?.takeUnless { it == "null" }?.let { Json.decodeFromString<OriginDto>(it) },
    payload = payload
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<PumpSettingsDataDto.PayloadDto>(it) },
)
