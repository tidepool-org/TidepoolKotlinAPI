package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import org.tidepool.sdk.model.BloodGlucose
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/**
 * A pump's settings. Mirrors a subset of TidepoolKit's TPumpSettingsDatum (the fields this SDK
 * actually needs; TidepoolKit has several more schedule/calculator fields this subset omits).
 */
data class PumpSettingsData(
    override val id: String,
    override val type: DataType = DataType.PumpSettings,
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
    val activeScheduleName: String? = null,
    val basal: Basal? = null,
    val basalRateSchedules: Map<String, List<ScheduleStart>>? = null,
    val bloodGlucoseSafetyLimit: Double? = null,
    val bloodGlucoseTargetPhysicalActivity: BloodGlucose.Target? = null,
    val bloodGlucoseTargetPreprandial: BloodGlucose.Target? = null,
    val bloodGlucoseTargetSchedules: Map<String, List<BloodGlucose.StartTarget>>? = null,
    val bolus: Bolus? = null,
    val carbohydrateRatioSchedules: Map<String, List<ScheduleStart>>? = null,
    val display: Display? = null,
    val firmwareVersion: String? = null,
    val hardwareVersion: String? = null,
    val insulinModel: InsulinModel? = null,
    val insulinSensitivitySchedules: Map<String, List<ScheduleStart>>? = null,
    val manufacturers: List<String>? = null,
    val model: String? = null,
    val name: String? = null,
    val overridePresets: Map<String, OverridePreset>? = null,
    val scheduleTimeZoneOffset: Int? = null,
    val softwareVersion: String? = null,
    val units: Units? = null,
    val origin: Origin? = null,
    val payload: Payload? = null,
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

    data class Basal(
        val rateMaximum: RateMaximum? = null,
    ) {
        data class RateMaximum(
            val value: Double? = null,
            val units: RateUnits? = null,
        )

        enum class RateUnits {
            UnitsPerHour,
        }
    }

    data class Bolus(
        val amountMaximum: AmountMaximum? = null,
    ) {
        data class AmountMaximum(
            val value: Double? = null,
            val units: Insulin.Units? = null,
        )
    }

    /** A schedule entry keyed by [start], milliseconds since midnight. */
    data class ScheduleStart(
        val start: Double? = null,
        val amount: Double? = null,
    )

    data class Display(
        val bloodGlucose: DisplayBloodGlucose? = null,
    ) {
        data class DisplayBloodGlucose(
            val units: BloodGlucose.Units? = null,
        )
    }

    data class InsulinModel(
        val modelType: ModelType? = null,
        /** Seconds. TidepoolKit encodes an Int number of seconds; platform caps this at 86400 (insulin_model.go). */
        val actionDelay: Double? = null,
        /** Seconds. TidepoolKit encodes an Int number of seconds; platform caps this at 86400 (insulin_model.go). */
        val actionDuration: Double? = null,
        /** Seconds. TidepoolKit encodes an Int number of seconds; platform caps this at 86400 (insulin_model.go). */
        val actionPeakOffset: Double? = null,
    ) {
        enum class ModelType {
            Fiasp,
            Other,
            RapidAdult,
            RapidChild,
            Walsh,
        }
    }

    data class OverridePreset(
        val abbreviation: String? = null,
        /** Milliseconds. */
        val duration: Double? = null,
        val bloodGlucoseTarget: BloodGlucose.Target? = null,
        val basalRateScaleFactor: Double? = null,
        val carbohydrateRatioScaleFactor: Double? = null,
        val insulinSensitivityScaleFactor: Double? = null,
    )

    data class Units(
        val bloodGlucose: BloodGlucose.Units? = null,
        val carbohydrate: DosingDecisionData.Units.Carb? = null,
        val insulin: Insulin.Units? = null,
    )

    data class Payload(
        val syncIdentifier: String,
    )
}
