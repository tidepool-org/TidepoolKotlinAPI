package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import org.tidepool.sdk.model.BloodGlucose
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/**
 * A device event datum: CGM sensor and transmitter lifecycle events, a pump's suspend and resume
 * (TidepoolKit's TStatusDeviceEventDatum) and a preset or pre-meal override
 * (TPumpSettingsOverrideDeviceEventDatum). Only the fields of its [subType] are set.
 */
data class DeviceEventData(
    override val id: String,
    override val type: DataType = DataType.DeviceEvent,
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>>? = null,
    override val associations: List<Association> = emptyList(),
    override val clockDriftOffset: Duration? = null,
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    override val timeZone: TimeZone? = null,
    override val timeZoneOffset: Int? = null,
    val subType: SubType,
    val deviceIdentifier: String? = null,
    /** seconds */
    val expectedLifetimeSeconds: Double? = null,
    /** seconds */
    val warmupPeriodSeconds: Double? = null,
    val failureMessage: String? = null,
    /**
     * Where the event came from. Uploading a datum with the same origin id replaces the earlier
     * one (the data set deduplicates by origin), so an override's later end replaces its start.
     */
    val origin: Origin? = null,
    val payload: Payload? = null,
    /** [SubType.Status]: whether delivery was suspended or resumed. */
    val status: Status? = null,
    /** [SubType.Status]: who suspended or resumed delivery. */
    val statusReason: StatusReason? = null,
    /** [SubType.PumpSettingsOverride] */
    val overrideType: OverrideType? = null,
    /** [SubType.PumpSettingsOverride]: the preset's name, for [OverrideType.Preset] only. */
    val overridePreset: String? = null,
    /** [SubType.PumpSettingsOverride] */
    val method: Method? = null,
    /** [SubType.PumpSettingsOverride]: milliseconds; null while an open-ended override is running. */
    val durationMillis: Long? = null,
    /** [SubType.PumpSettingsOverride]: the correction range, in [bloodGlucoseUnits]. */
    val bloodGlucoseTarget: BloodGlucose.Target? = null,
    /** [SubType.PumpSettingsOverride]: set exactly when [bloodGlucoseTarget] is. */
    val bloodGlucoseUnits: BloodGlucose.Units? = null,
    val basalRateScaleFactor: Double? = null,
    val carbRatioScaleFactor: Double? = null,
    val insulinSensitivityScaleFactor: Double? = null,
) : BaseData(
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
) {
    enum class SubType {
        SensorStart,
        SensorEnd,
        TransmitterStart,
        TransmitterEnd,
        Status,
        PumpSettingsOverride,
    }

    enum class Status {
        Suspended,
        Resumed,
    }

    enum class StatusReason {
        Automatic,
        Manual,
    }

    enum class OverrideType {
        Custom,
        PhysicalActivity,
        Preprandial,
        Preset,
        Sleep,
    }

    enum class Method {
        Automatic,
        Manual,
    }

    data class Payload(
        val syncIdentifier: String,
    )
}
