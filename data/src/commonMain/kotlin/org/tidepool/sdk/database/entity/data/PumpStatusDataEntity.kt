package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.data.OriginDto
import org.tidepool.sdk.dto.data.PumpStatusDataDto
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

// Not yet wired into LoopKitDatabase: it gets its table in the outbox's v3->v4 migration.
@Entity(tableName = "pump_status_data")
data class PumpStatusDataEntity(
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
    override var timeZoneOffset: Int? = null,

    @ColumnInfo(name = "basal_delivery")
    var basalDelivery: String? = null, // JSON string
    @ColumnInfo(name = "battery")
    var battery: String? = null, // JSON string
    @ColumnInfo(name = "bolus_delivery")
    var bolusDelivery: String? = null, // JSON string
    @ColumnInfo(name = "delivery_indeterminant")
    var deliveryIndeterminant: Boolean? = null,
    @ColumnInfo(name = "reservoir")
    var reservoir: String? = null, // JSON string
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

fun PumpStatusDataDto.toEntity() = PumpStatusDataEntity(
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
    basalDelivery = basalDelivery?.let { Json.encodeToString(it) },
    battery = battery?.let { Json.encodeToString(it) },
    bolusDelivery = bolusDelivery?.let { Json.encodeToString(it) },
    deliveryIndeterminant = deliveryIndeterminant,
    reservoir = reservoir?.let { Json.encodeToString(it) },
    origin = origin?.let { Json.encodeToString(it) },
    payload = payload?.let { Json.encodeToString(it) },
)

fun PumpStatusDataEntity.toDto() = PumpStatusDataDto(
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
    basalDelivery = basalDelivery
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<PumpStatusDataDto.BasalDeliveryDto>(it) },
    battery = battery
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<PumpStatusDataDto.BatteryDto>(it) },
    bolusDelivery = bolusDelivery
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<PumpStatusDataDto.BolusDeliveryDto>(it) },
    deliveryIndeterminant = deliveryIndeterminant,
    reservoir = reservoir
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<PumpStatusDataDto.ReservoirDto>(it) },
    origin = origin?.takeUnless { it == "null" }?.let { Json.decodeFromString<OriginDto>(it) },
    payload = payload
        ?.takeUnless { it == "null" }
        ?.let { Json.decodeFromString<PumpStatusDataDto.PayloadDto>(it) },
)
