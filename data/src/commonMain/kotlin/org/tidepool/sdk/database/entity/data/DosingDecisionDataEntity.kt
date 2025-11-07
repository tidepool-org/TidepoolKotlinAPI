package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.tidepool.sdk.deserialization.InstantSerializer
import org.tidepool.sdk.deserialization.TimeZoneSerializer
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.data.DosingDecisionDataDto
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

@Entity(tableName = "dosing_decision_data")
data class DosingDecisionDataEntity(
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

    @ColumnInfo(name = "reason")
    var reason: String = "",
    @ColumnInfo(name = "carbs_on_board")
    var carbsOnBoard: String? = null, // JSON string
    @ColumnInfo(name = "insulin_on_board")
    var insulinOnBoard: String? = null, // JSON string
    @ColumnInfo(name = "recommended_basal")
    var recommendedBasal: String? = null, // JSON string
    @ColumnInfo(name = "recommended_bolus")
    var recommendedBolus: String? = null, // JSON string
    @ColumnInfo(name = "requested_bolus")
    var requestedBolus: String? = null, // JSON string
    @ColumnInfo(name = "schedule_time_zone_offset")
    var scheduleTimeZoneOffset: Int? = null,
    @ColumnInfo(name = "units")
    var units: String = "", // JSON string
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

fun DosingDecisionDataDto.toEntity(): DosingDecisionDataEntity {
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        classDiscriminator =
            "__type"  // Use different discriminator to avoid conflict with 'type' property
        serializersModule = SerializersModule {
            contextual(Instant::class, InstantSerializer)
            contextual(TimeZone::class, TimeZoneSerializer)
            // Configure BaseData polymorphism
        }
    }

    return DosingDecisionDataEntity(
        id = id,
        type = json.encodeToString(type),
        time = time?.epochSecond,
        annotations = annotations.let { json.encodeToString(it) },
        associations = associations.let { json.encodeToString(it) },
        clockDriftOffset = clockDriftOffset?.inWholeMilliseconds,
        conversionOffset = conversionOffset?.inWholeMilliseconds,
        dataSetId = dataSetId,
        deviceTime = deviceTime,
        notes = notes.let { json.encodeToString(it) },
        timeZone = timeZone?.id,
        timeZoneOffset = timeZoneOffset,
        reason = reason,
        carbsOnBoard = carbsOnBoard?.let { json.encodeToString(it) },
        insulinOnBoard = insulinOnBoard?.let { json.encodeToString(it) },
        recommendedBasal = recommendedBasal?.let { Json.encodeToString(it) },
        recommendedBolus = recommendedBolus?.let { json.encodeToString(it) },
        requestedBolus = requestedBolus?.let { json.encodeToString(it) },
        scheduleTimeZoneOffset = scheduleTimeZoneOffset,
        units = json.encodeToString(units),
    )
}

fun DosingDecisionDataEntity.toDto() = DosingDecisionDataDto(
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
    reason = reason,
    carbsOnBoard = carbsOnBoard?.let { Json.decodeFromString(it) },
    insulinOnBoard = insulinOnBoard?.let { Json.decodeFromString(it) },
    recommendedBasal = recommendedBasal?.let { Json.decodeFromString(it) },
    recommendedBolus = recommendedBolus?.let { Json.decodeFromString(it) },
    requestedBolus = requestedBolus?.let { Json.decodeFromString(it) },
    scheduleTimeZoneOffset = scheduleTimeZoneOffset,
    units = Json.decodeFromString(units),
)
