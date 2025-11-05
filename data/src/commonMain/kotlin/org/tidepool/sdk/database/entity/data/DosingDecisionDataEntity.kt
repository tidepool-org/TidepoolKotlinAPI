package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.tidepool.sdk.deserialization.InstantSerializer
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
        SerializersModule {
            contextual(Instant::class, InstantSerializer)
        }
    }
    return DosingDecisionDataEntity(
        id = id,
        type = json.encodeToString(type),
        time = time,
        annotations = annotations.let { json.encodeToString(it) },
        associations = associations.let { json.encodeToString(it) },
        clockDriftOffset = clockDriftOffset?.inWholeMilliseconds,
        conversionOffset = conversionOffset?.inWholeMilliseconds,
        dataSetId = dataSetId,
        deviceTime = deviceTime,
        notes = notes.let { json.encodeToString(it) },
        timeZone = timeZone?.id,
        timeZoneOffset = timeZoneOffset?.inWholeMilliseconds,
        reason = reason,
        carbsOnBoard = carbsOnBoard?.let { json.encodeToString(it) },
        insulinOnBoard = insulinOnBoard?.let { json.encodeToString(it) },
        recommendedBasal = recommendedBasal?.let { json.encodeToString(it) },
        recommendedBolus = recommendedBolus?.let { json.encodeToString(it) },
        requestedBolus = requestedBolus?.let { json.encodeToString(it) },
        scheduleTimeZoneOffset = scheduleTimeZoneOffset,
        units = json.encodeToString(units),
    )
}

fun DosingDecisionDataEntity.toDto(): DosingDecisionDataDto {
    val json = Json {
        SerializersModule {
            contextual(Instant::class, InstantSerializer)
        }
    }

    return DosingDecisionDataDto(
        id = id,
        type = json.decodeFromString(type),
        time = time,
        annotations = annotations
            ?.let { json.decodeFromString<List<Map<String, String>>>(it) }
            .orEmpty(),
        associations = associations
            ?.let { json.decodeFromString<List<AssociationDto>>(it) }
            .orEmpty(),
        clockDriftOffset = clockDriftOffset?.milliseconds,
        conversionOffset = conversionOffset?.milliseconds,
        dataSetId = dataSetId,
        deviceTime = deviceTime,
        notes = notes
            ?.let { json.decodeFromString<List<String>>(it) }
            .orEmpty(),
        timeZone = timeZone?.let { TimeZone.getTimeZone(it) },
        timeZoneOffset = timeZoneOffset?.milliseconds,
        reason = reason,
        carbsOnBoard = carbsOnBoard?.let { json.decodeFromString(it) },
        insulinOnBoard = insulinOnBoard?.let { json.decodeFromString(it) },
        recommendedBasal = recommendedBasal?.let { json.decodeFromString(it) },
        recommendedBolus = recommendedBolus?.let { json.decodeFromString(it) },
        requestedBolus = requestedBolus?.let { json.decodeFromString(it) },
        scheduleTimeZoneOffset = scheduleTimeZoneOffset,
        units = json.decodeFromString(units),
    )
}
