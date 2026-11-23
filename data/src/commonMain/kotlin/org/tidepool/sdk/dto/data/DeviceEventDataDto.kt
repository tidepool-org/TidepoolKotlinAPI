package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.DeviceEventData
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/**
 * Mirrors TidepoolKit's device event datums: the CGM lifecycle events,
 * TStatusDeviceEventDatum (`status`) and TPumpSettingsOverrideDeviceEventDatum
 * (`pumpSettingsOverride`).
 */
@Serializable
data class DeviceEventDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.DeviceEvent,
    @Contextual
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>>? = null,
    override val associations: List<AssociationDto> = emptyList(),
    @Contextual
    override val clockDriftOffset: Duration? = null,
    @Contextual
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    @Contextual
    @SerialName("timezone")
    override val timeZone: TimeZone? = null,
    @Contextual
    @SerialName("timezoneOffset")
    override val timeZoneOffset: Int? = null,

    @SerialName("subType")
    val subType: SubTypeDto,
    @SerialName("deviceIdentifier")
    val deviceIdentifier: String? = null,
    @SerialName("expectedLifetimeSeconds")
    val expectedLifetimeSeconds: Double? = null,
    @SerialName("warmupPeriodSeconds")
    val warmupPeriodSeconds: Double? = null,
    @SerialName("failureMessage")
    val failureMessage: String? = null,
    @SerialName("origin")
    val origin: OriginDto? = null,
    @SerialName("payload")
    val payload: PayloadDto? = null,
    @SerialName("status")
    val status: StatusDto? = null,
    /** TidepoolKit's reason: the status as the key, e.g. `{"suspended": "manual"}`. */
    @SerialName("reason")
    val reason: Map<StatusDto, StatusReasonDto>? = null,
    @SerialName("overrideType")
    val overrideType: OverrideTypeDto? = null,
    @SerialName("overridePreset")
    val overridePreset: String? = null,
    @SerialName("method")
    val method: MethodDto? = null,
    /** Milliseconds, as the platform's pumpSettingsOverride validator takes it. */
    @SerialName("duration")
    val duration: Long? = null,
    @SerialName("bgTarget")
    val bgTarget: BloodGlucoseDto.TargetDto? = null,
    @SerialName("units")
    val units: UnitsDto? = null,
    @SerialName("basalRateScaleFactor")
    val basalRateScaleFactor: Double? = null,
    @SerialName("carbRatioScaleFactor")
    val carbRatioScaleFactor: Double? = null,
    @SerialName("insulinSensitivityScaleFactor")
    val insulinSensitivityScaleFactor: Double? = null,
) : BaseDataDto() {

    @Serializable
    enum class SubTypeDto {
        @SerialName("sensorStart")
        SensorStart,

        @SerialName("sensorEnd")
        SensorEnd,

        @SerialName("transmitterStart")
        TransmitterStart,

        @SerialName("transmitterEnd")
        TransmitterEnd,

        @SerialName("status")
        Status,

        @SerialName("pumpSettingsOverride")
        PumpSettingsOverride,
    }

    @Serializable
    enum class StatusDto {
        @SerialName("suspended")
        Suspended,

        @SerialName("resumed")
        Resumed,
    }

    @Serializable
    enum class StatusReasonDto {
        @SerialName("automatic")
        Automatic,

        @SerialName("manual")
        Manual,
    }

    @Serializable
    enum class OverrideTypeDto {
        @SerialName("custom")
        Custom,

        @SerialName("physicalActivity")
        PhysicalActivity,

        @SerialName("preprandial")
        Preprandial,

        @SerialName("preset")
        Preset,

        @SerialName("sleep")
        Sleep,
    }

    @Serializable
    enum class MethodDto {
        @SerialName("automatic")
        Automatic,

        @SerialName("manual")
        Manual,
    }

    @Serializable
    data class PayloadDto(
        @SerialName("syncIdentifier")
        val syncIdentifier: String,
    )

    @Serializable
    data class UnitsDto(
        @SerialName("bg")
        val bloodGlucose: BloodGlucoseDto.UnitsDto,
    )
}

internal fun DeviceEventDataDto.SubTypeDto.toDomain(): DeviceEventData.SubType = when (this) {
    DeviceEventDataDto.SubTypeDto.SensorStart -> DeviceEventData.SubType.SensorStart
    DeviceEventDataDto.SubTypeDto.SensorEnd -> DeviceEventData.SubType.SensorEnd
    DeviceEventDataDto.SubTypeDto.TransmitterStart -> DeviceEventData.SubType.TransmitterStart
    DeviceEventDataDto.SubTypeDto.TransmitterEnd -> DeviceEventData.SubType.TransmitterEnd
    DeviceEventDataDto.SubTypeDto.Status -> DeviceEventData.SubType.Status
    DeviceEventDataDto.SubTypeDto.PumpSettingsOverride -> DeviceEventData.SubType.PumpSettingsOverride
}

internal fun DeviceEventData.SubType.toDto(): DeviceEventDataDto.SubTypeDto = when (this) {
    DeviceEventData.SubType.SensorStart -> DeviceEventDataDto.SubTypeDto.SensorStart
    DeviceEventData.SubType.SensorEnd -> DeviceEventDataDto.SubTypeDto.SensorEnd
    DeviceEventData.SubType.TransmitterStart -> DeviceEventDataDto.SubTypeDto.TransmitterStart
    DeviceEventData.SubType.TransmitterEnd -> DeviceEventDataDto.SubTypeDto.TransmitterEnd
    DeviceEventData.SubType.Status -> DeviceEventDataDto.SubTypeDto.Status
    DeviceEventData.SubType.PumpSettingsOverride -> DeviceEventDataDto.SubTypeDto.PumpSettingsOverride
}

private fun DeviceEventDataDto.StatusDto.toDomain(): DeviceEventData.Status = when (this) {
    DeviceEventDataDto.StatusDto.Suspended -> DeviceEventData.Status.Suspended
    DeviceEventDataDto.StatusDto.Resumed -> DeviceEventData.Status.Resumed
}

private fun DeviceEventData.Status.toDto(): DeviceEventDataDto.StatusDto = when (this) {
    DeviceEventData.Status.Suspended -> DeviceEventDataDto.StatusDto.Suspended
    DeviceEventData.Status.Resumed -> DeviceEventDataDto.StatusDto.Resumed
}

private fun DeviceEventDataDto.StatusReasonDto.toDomain(): DeviceEventData.StatusReason = when (this) {
    DeviceEventDataDto.StatusReasonDto.Automatic -> DeviceEventData.StatusReason.Automatic
    DeviceEventDataDto.StatusReasonDto.Manual -> DeviceEventData.StatusReason.Manual
}

private fun DeviceEventData.StatusReason.toDto(): DeviceEventDataDto.StatusReasonDto = when (this) {
    DeviceEventData.StatusReason.Automatic -> DeviceEventDataDto.StatusReasonDto.Automatic
    DeviceEventData.StatusReason.Manual -> DeviceEventDataDto.StatusReasonDto.Manual
}

private fun DeviceEventDataDto.OverrideTypeDto.toDomain(): DeviceEventData.OverrideType = when (this) {
    DeviceEventDataDto.OverrideTypeDto.Custom -> DeviceEventData.OverrideType.Custom
    DeviceEventDataDto.OverrideTypeDto.PhysicalActivity -> DeviceEventData.OverrideType.PhysicalActivity
    DeviceEventDataDto.OverrideTypeDto.Preprandial -> DeviceEventData.OverrideType.Preprandial
    DeviceEventDataDto.OverrideTypeDto.Preset -> DeviceEventData.OverrideType.Preset
    DeviceEventDataDto.OverrideTypeDto.Sleep -> DeviceEventData.OverrideType.Sleep
}

private fun DeviceEventData.OverrideType.toDto(): DeviceEventDataDto.OverrideTypeDto = when (this) {
    DeviceEventData.OverrideType.Custom -> DeviceEventDataDto.OverrideTypeDto.Custom
    DeviceEventData.OverrideType.PhysicalActivity -> DeviceEventDataDto.OverrideTypeDto.PhysicalActivity
    DeviceEventData.OverrideType.Preprandial -> DeviceEventDataDto.OverrideTypeDto.Preprandial
    DeviceEventData.OverrideType.Preset -> DeviceEventDataDto.OverrideTypeDto.Preset
    DeviceEventData.OverrideType.Sleep -> DeviceEventDataDto.OverrideTypeDto.Sleep
}

private fun DeviceEventDataDto.MethodDto.toDomain(): DeviceEventData.Method = when (this) {
    DeviceEventDataDto.MethodDto.Automatic -> DeviceEventData.Method.Automatic
    DeviceEventDataDto.MethodDto.Manual -> DeviceEventData.Method.Manual
}

private fun DeviceEventData.Method.toDto(): DeviceEventDataDto.MethodDto = when (this) {
    DeviceEventData.Method.Automatic -> DeviceEventDataDto.MethodDto.Automatic
    DeviceEventData.Method.Manual -> DeviceEventDataDto.MethodDto.Manual
}

fun DeviceEventDataDto.toDomain(): DeviceEventData = DeviceEventData(
    id = id,
    type = type.toDomain(),
    time = time,
    annotations = annotations,
    associations = associations.map { it.toDomain() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    subType = subType.toDomain(),
    deviceIdentifier = deviceIdentifier,
    expectedLifetimeSeconds = expectedLifetimeSeconds,
    warmupPeriodSeconds = warmupPeriodSeconds,
    failureMessage = failureMessage,
    origin = origin?.toDomain(),
    payload = payload?.let { DeviceEventData.Payload(syncIdentifier = it.syncIdentifier) },
    status = status?.toDomain(),
    statusReason = status?.let { reason?.get(it) }?.toDomain(),
    overrideType = overrideType?.toDomain(),
    overridePreset = overridePreset,
    method = method?.toDomain(),
    durationMillis = duration,
    bloodGlucoseTarget = bgTarget?.toDomain(),
    bloodGlucoseUnits = units?.bloodGlucose?.toDomain(),
    basalRateScaleFactor = basalRateScaleFactor,
    carbRatioScaleFactor = carbRatioScaleFactor,
    insulinSensitivityScaleFactor = insulinSensitivityScaleFactor,
)

fun DeviceEventData.toDto(): DeviceEventDataDto = DeviceEventDataDto(
    id = id,
    type = type.toDto(),
    time = time,
    annotations = annotations?.takeUnless { it.isEmpty() },
    associations = associations.map { it.toDto() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    subType = subType.toDto(),
    deviceIdentifier = deviceIdentifier,
    expectedLifetimeSeconds = expectedLifetimeSeconds,
    warmupPeriodSeconds = warmupPeriodSeconds,
    failureMessage = failureMessage,
    origin = origin?.toDto(),
    payload = payload?.let { DeviceEventDataDto.PayloadDto(syncIdentifier = it.syncIdentifier) },
    status = status?.toDto(),
    reason = status?.let { status -> statusReason?.let { mapOf(status.toDto() to it.toDto()) } },
    overrideType = overrideType?.toDto(),
    overridePreset = overridePreset,
    method = method?.toDto(),
    duration = durationMillis,
    bgTarget = bloodGlucoseTarget?.toDto(),
    units = bloodGlucoseUnits?.let { DeviceEventDataDto.UnitsDto(bloodGlucose = it.toDto()) },
    basalRateScaleFactor = basalRateScaleFactor,
    carbRatioScaleFactor = carbRatioScaleFactor,
    insulinSensitivityScaleFactor = insulinSensitivityScaleFactor,
)
