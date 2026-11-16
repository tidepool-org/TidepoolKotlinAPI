package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.Insulin
import org.tidepool.sdk.model.data.PumpSettingsData
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/**
 * Mirrors a subset of TidepoolKit's TPumpSettingsDatum (the fields this SDK actually needs;
 * TidepoolKit has several more schedule/calculator fields this subset omits).
 */
@Serializable
data class PumpSettingsDataDto(
    override val id: String = "",
    override val type: BaseDataDto.DataTypeDto = BaseDataDto.DataTypeDto.PumpSettings,
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

    @SerialName("activeSchedule")
    val activeScheduleName: String? = null,
    @SerialName("automatedDelivery")
    val automatedDelivery: Boolean? = null,
    @SerialName("basal")
    val basal: BasalDto? = null,
    @SerialName("basalSchedules")
    val basalRateSchedules: Map<String, List<ScheduleStartDto>>? = null,
    @SerialName("bgSafetyLimit")
    val bloodGlucoseSafetyLimit: Double? = null,
    @SerialName("bgTargetPhysicalActivity")
    val bloodGlucoseTargetPhysicalActivity: BloodGlucoseTargetDto? = null,
    @SerialName("bgTargetPreprandial")
    val bloodGlucoseTargetPreprandial: BloodGlucoseTargetDto? = null,
    @SerialName("bgTargets")
    val bloodGlucoseTargetSchedules: Map<String, List<BloodGlucoseScheduleStartDto>>? = null,
    @SerialName("bolus")
    val bolus: BolusDto? = null,
    @SerialName("carbRatios")
    val carbohydrateRatioSchedules: Map<String, List<ScheduleStartDto>>? = null,
    @SerialName("display")
    val display: DisplayDto? = null,
    @SerialName("firmwareVersion")
    val firmwareVersion: String? = null,
    @SerialName("hardwareVersion")
    val hardwareVersion: String? = null,
    @SerialName("insulinFormulation")
    val insulinFormulation: String? = null,
    @SerialName("insulinModel")
    val insulinModel: InsulinModelDto? = null,
    @SerialName("insulinSensitivities")
    val insulinSensitivitySchedules: Map<String, List<ScheduleStartDto>>? = null,
    @SerialName("manufacturers")
    val manufacturers: List<String>? = null,
    @SerialName("model")
    val model: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("overridePresets")
    val overridePresets: Map<String, OverridePresetDto>? = null,
    @SerialName("scheduleTimeZoneOffset")
    val scheduleTimeZoneOffset: Int? = null,
    @SerialName("serialNumber")
    val serialNumber: String? = null,
    @SerialName("softwareVersion")
    val softwareVersion: String? = null,
    @SerialName("units")
    val units: UnitsDto? = null,
    @SerialName("origin")
    val origin: OriginDto? = null,
    @SerialName("payload")
    val payload: PayloadDto? = null,
) : BaseDataDto() {

    @Serializable
    data class BasalDto(
        @SerialName("rateMaximum")
        val rateMaximum: RateMaximumDto? = null,
    ) {
        @Serializable
        data class RateMaximumDto(
            @SerialName("value")
            val value: Double? = null,
            @SerialName("units")
            val units: RateUnitsDto? = null,
        )

        @Serializable
        enum class RateUnitsDto {
            @SerialName("Units/hour")
            UnitsPerHour,
        }
    }

    @Serializable
    data class BolusDto(
        @SerialName("amountMaximum")
        val amountMaximum: AmountMaximumDto? = null,
    ) {
        @Serializable
        data class AmountMaximumDto(
            @SerialName("value")
            val value: Double? = null,
            @SerialName("units")
            val units: InsulinDto.UnitsDto? = null,
        )
    }

    /** A schedule entry keyed by [start], milliseconds since midnight. */
    @Serializable
    data class ScheduleStartDto(
        @SerialName("start")
        val start: Long? = null,
        @SerialName("rate")
        val rate: Double? = null,
        @SerialName("amount")
        val amount: Double? = null,
    )

    @Serializable
    /** TidepoolKit's `TBloodGlucose.StartTarget`: the target's fields sit beside [start], not nested. */
    data class BloodGlucoseScheduleStartDto(
        @SerialName("start")
        val start: Long? = null,
        @SerialName("low")
        val low: Double? = null,
        @SerialName("high")
        val high: Double? = null,
        @SerialName("target")
        val target: Double? = null,
        @SerialName("range")
        val range: Double? = null,
    )

    @Serializable
    data class BloodGlucoseTargetDto(
        @SerialName("low")
        val low: Double? = null,
        @SerialName("high")
        val high: Double? = null,
        @SerialName("target")
        val target: Double? = null,
        @SerialName("range")
        val range: Double? = null,
    )

    @Serializable
    data class DisplayDto(
        @SerialName("bloodGlucose")
        val bloodGlucose: DisplayBloodGlucoseDto? = null,
    ) {
        @Serializable
        data class DisplayBloodGlucoseDto(
            @SerialName("units")
            val units: BloodGlucoseDto.UnitsDto? = null,
        )
    }

    @Serializable
    data class InsulinModelDto(
        @SerialName("modelType")
        val modelType: ModelTypeDto? = null,
        @SerialName("actionDelay")
        val actionDelay: Long? = null,
        @SerialName("actionDuration")
        val actionDuration: Long? = null,
        @SerialName("actionPeakOffset")
        val actionPeakOffset: Long? = null,
    ) {
        @Serializable
        enum class ModelTypeDto {
            @SerialName("fiasp")
            Fiasp,
            @SerialName("other")
            Other,
            @SerialName("rapidAdult")
            RapidAdult,
            @SerialName("rapidChild")
            RapidChild,
            @SerialName("walsh")
            Walsh,
        }
    }

    @Serializable
    data class OverridePresetDto(
        @SerialName("abbreviation")
        val abbreviation: String? = null,
        @SerialName("duration")
        val duration: Long? = null,
        @SerialName("bgTarget")
        val bloodGlucoseTarget: BloodGlucoseTargetDto? = null,
        @SerialName("basalRateScaleFactor")
        val basalRateScaleFactor: Double? = null,
        @SerialName("carbRatioScaleFactor")
        val carbohydrateRatioScaleFactor: Double? = null,
        @SerialName("insulinSensitivityScaleFactor")
        val insulinSensitivityScaleFactor: Double? = null,
    )

    @Serializable
    data class UnitsDto(
        @SerialName("bg")
        val bloodGlucose: BloodGlucoseDto.UnitsDto? = null,
        @SerialName("carb")
        val carbohydrate: DosingDecisionDataDto.UnitsDto.CarbDto? = null,
        @SerialName("insulin")
        val insulin: InsulinDto.UnitsDto? = null,
    )

    @Serializable
    data class PayloadDto(
        @SerialName("syncIdentifier")
        val syncIdentifier: String,
    )
}

fun PumpSettingsDataDto.toDomain(): PumpSettingsData = PumpSettingsData(
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
    activeScheduleName = activeScheduleName,
    automatedDelivery = automatedDelivery,
    basal = basal?.toDomain(),
    basalRateSchedules = basalRateSchedules?.mapValues { (_, v) -> v.map { it.toScheduleStartDomain() } },
    bloodGlucoseSafetyLimit = bloodGlucoseSafetyLimit,
    bloodGlucoseTargetPhysicalActivity = bloodGlucoseTargetPhysicalActivity?.toDomain(),
    bloodGlucoseTargetPreprandial = bloodGlucoseTargetPreprandial?.toDomain(),
    bloodGlucoseTargetSchedules = bloodGlucoseTargetSchedules?.mapValues { (_, v) -> v.map { it.toDomain() } },
    bolus = bolus?.toDomain(),
    carbohydrateRatioSchedules = carbohydrateRatioSchedules?.mapValues { (_, v) -> v.map { it.toScheduleStartDomain() } },
    display = display?.toDomain(),
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    insulinFormulation = insulinFormulation,
    insulinModel = insulinModel?.toDomain(),
    insulinSensitivitySchedules = insulinSensitivitySchedules?.mapValues { (_, v) -> v.map { it.toScheduleStartDomain() } },
    manufacturers = manufacturers,
    model = model,
    name = name,
    overridePresets = overridePresets?.mapValues { (_, v) -> v.toDomain() },
    scheduleTimeZoneOffset = scheduleTimeZoneOffset,
    serialNumber = serialNumber,
    softwareVersion = softwareVersion,
    units = units?.toDomain(),
    origin = origin?.toDomain(),
    payload = payload?.toDomain(),
)

fun PumpSettingsData.toDto(): PumpSettingsDataDto = PumpSettingsDataDto(
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
    activeScheduleName = activeScheduleName,
    automatedDelivery = automatedDelivery,
    basal = basal?.toDto(),
    basalRateSchedules = basalRateSchedules?.mapValues { (_, v) -> v.map { it.toBasalRateStartDto() } },
    bloodGlucoseSafetyLimit = bloodGlucoseSafetyLimit,
    bloodGlucoseTargetPhysicalActivity = bloodGlucoseTargetPhysicalActivity?.toDto(),
    bloodGlucoseTargetPreprandial = bloodGlucoseTargetPreprandial?.toDto(),
    bloodGlucoseTargetSchedules = bloodGlucoseTargetSchedules?.mapValues { (_, v) -> v.map { it.toDto() } },
    bolus = bolus?.toDto(),
    carbohydrateRatioSchedules = carbohydrateRatioSchedules?.mapValues { (_, v) -> v.map { it.toScheduleStartDto() } },
    display = display?.toDto(),
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    insulinFormulation = insulinFormulation,
    insulinModel = insulinModel?.toDto(),
    insulinSensitivitySchedules = insulinSensitivitySchedules?.mapValues { (_, v) -> v.map { it.toScheduleStartDto() } },
    manufacturers = manufacturers,
    model = model,
    name = name,
    overridePresets = overridePresets?.mapValues { (_, v) -> v.toDto() },
    scheduleTimeZoneOffset = scheduleTimeZoneOffset,
    serialNumber = serialNumber,
    softwareVersion = softwareVersion,
    units = units?.toDto(),
    origin = origin?.toDto(),
    payload = payload?.toDto(),
)

private fun PumpSettingsDataDto.BasalDto.RateUnitsDto.toDomain(): PumpSettingsData.Basal.RateUnits = when (this) {
    PumpSettingsDataDto.BasalDto.RateUnitsDto.UnitsPerHour -> PumpSettingsData.Basal.RateUnits.UnitsPerHour
}

private fun PumpSettingsData.Basal.RateUnits.toDto(): PumpSettingsDataDto.BasalDto.RateUnitsDto = when (this) {
    PumpSettingsData.Basal.RateUnits.UnitsPerHour -> PumpSettingsDataDto.BasalDto.RateUnitsDto.UnitsPerHour
}

private fun PumpSettingsDataDto.BasalDto.RateMaximumDto.toDomain() = PumpSettingsData.Basal.RateMaximum(
    value = value,
    units = units?.toDomain(),
)

private fun PumpSettingsData.Basal.RateMaximum.toDto() = PumpSettingsDataDto.BasalDto.RateMaximumDto(
    value = value,
    units = units?.toDto(),
)

private fun PumpSettingsDataDto.BasalDto.toDomain() = PumpSettingsData.Basal(rateMaximum = rateMaximum?.toDomain())

private fun PumpSettingsData.Basal.toDto() = PumpSettingsDataDto.BasalDto(rateMaximum = rateMaximum?.toDto())

private fun InsulinDto.UnitsDto.toDomain(): Insulin.Units = when (this) {
    InsulinDto.UnitsDto.Units -> Insulin.Units.Units
}

private fun Insulin.Units.toDto(): InsulinDto.UnitsDto = when (this) {
    Insulin.Units.Units -> InsulinDto.UnitsDto.Units
}

private fun PumpSettingsDataDto.BolusDto.AmountMaximumDto.toDomain() = PumpSettingsData.Bolus.AmountMaximum(
    value = value,
    units = units?.toDomain(),
)

private fun PumpSettingsData.Bolus.AmountMaximum.toDto() = PumpSettingsDataDto.BolusDto.AmountMaximumDto(
    value = value,
    units = units?.toDto(),
)

private fun PumpSettingsDataDto.BolusDto.toDomain() = PumpSettingsData.Bolus(amountMaximum = amountMaximum?.toDomain())

private fun PumpSettingsData.Bolus.toDto() = PumpSettingsDataDto.BolusDto(amountMaximum = amountMaximum?.toDto())

private fun PumpSettingsDataDto.ScheduleStartDto.toScheduleStartDomain() = PumpSettingsData.ScheduleStart(
    start = start?.toDouble(),
    amount = rate ?: amount,
)

private fun PumpSettingsData.ScheduleStart.toScheduleStartDto() = PumpSettingsDataDto.ScheduleStartDto(
    start = start?.toLong(),
    amount = amount,
)

/** TidepoolKit's `BasalRateStart` names its value `rate`; carb ratio and sensitivity entries use `amount`. */
private fun PumpSettingsData.ScheduleStart.toBasalRateStartDto() = PumpSettingsDataDto.ScheduleStartDto(
    start = start?.toLong(),
    rate = amount,
)

private fun PumpSettingsDataDto.BloodGlucoseTargetDto.toDomain() = PumpSettingsData.BloodGlucoseTarget(
    low = low,
    high = high,
    target = target,
    range = range,
)

private fun PumpSettingsData.BloodGlucoseTarget.toDto() = PumpSettingsDataDto.BloodGlucoseTargetDto(
    low = low,
    high = high,
    target = target,
    range = range,
)

private fun PumpSettingsDataDto.BloodGlucoseScheduleStartDto.toDomain() = PumpSettingsData.BloodGlucoseScheduleStart(
    start = start?.toDouble(),
    target = PumpSettingsData.BloodGlucoseTarget(low = low, high = high, target = target, range = range),
)

private fun PumpSettingsData.BloodGlucoseScheduleStart.toDto() = PumpSettingsDataDto.BloodGlucoseScheduleStartDto(
    start = start?.toLong(),
    low = target?.low,
    high = target?.high,
    target = target?.target,
    range = target?.range,
)

private fun PumpSettingsDataDto.DisplayDto.DisplayBloodGlucoseDto.toDomain() =
    PumpSettingsData.Display.DisplayBloodGlucose(units = units?.toDomain())

private fun PumpSettingsData.Display.DisplayBloodGlucose.toDto() =
    PumpSettingsDataDto.DisplayDto.DisplayBloodGlucoseDto(units = units?.toDto())

private fun PumpSettingsDataDto.DisplayDto.toDomain() = PumpSettingsData.Display(bloodGlucose = bloodGlucose?.toDomain())

private fun PumpSettingsData.Display.toDto() = PumpSettingsDataDto.DisplayDto(bloodGlucose = bloodGlucose?.toDto())

private fun PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.toDomain(): PumpSettingsData.InsulinModel.ModelType = when (this) {
    PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.Fiasp -> PumpSettingsData.InsulinModel.ModelType.Fiasp
    PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.Other -> PumpSettingsData.InsulinModel.ModelType.Other
    PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.RapidAdult -> PumpSettingsData.InsulinModel.ModelType.RapidAdult
    PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.RapidChild -> PumpSettingsData.InsulinModel.ModelType.RapidChild
    PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.Walsh -> PumpSettingsData.InsulinModel.ModelType.Walsh
}

private fun PumpSettingsData.InsulinModel.ModelType.toDto(): PumpSettingsDataDto.InsulinModelDto.ModelTypeDto = when (this) {
    PumpSettingsData.InsulinModel.ModelType.Fiasp -> PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.Fiasp
    PumpSettingsData.InsulinModel.ModelType.Other -> PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.Other
    PumpSettingsData.InsulinModel.ModelType.RapidAdult -> PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.RapidAdult
    PumpSettingsData.InsulinModel.ModelType.RapidChild -> PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.RapidChild
    PumpSettingsData.InsulinModel.ModelType.Walsh -> PumpSettingsDataDto.InsulinModelDto.ModelTypeDto.Walsh
}

private fun PumpSettingsDataDto.InsulinModelDto.toDomain() = PumpSettingsData.InsulinModel(
    modelType = modelType?.toDomain(),
    actionDelay = actionDelay?.toDouble(),
    actionDuration = actionDuration?.toDouble(),
    actionPeakOffset = actionPeakOffset?.toDouble(),
)

private fun PumpSettingsData.InsulinModel.toDto() = PumpSettingsDataDto.InsulinModelDto(
    modelType = modelType?.toDto(),
    actionDelay = actionDelay?.toLong(),
    actionDuration = actionDuration?.toLong(),
    actionPeakOffset = actionPeakOffset?.toLong(),
)

private fun PumpSettingsDataDto.OverridePresetDto.toDomain() = PumpSettingsData.OverridePreset(
    abbreviation = abbreviation,
    duration = duration?.toDouble(),
    bloodGlucoseTarget = bloodGlucoseTarget?.toDomain(),
    basalRateScaleFactor = basalRateScaleFactor,
    carbohydrateRatioScaleFactor = carbohydrateRatioScaleFactor,
    insulinSensitivityScaleFactor = insulinSensitivityScaleFactor,
)

private fun PumpSettingsData.OverridePreset.toDto() = PumpSettingsDataDto.OverridePresetDto(
    abbreviation = abbreviation,
    duration = duration?.toLong(),
    bloodGlucoseTarget = bloodGlucoseTarget?.toDto(),
    basalRateScaleFactor = basalRateScaleFactor,
    carbohydrateRatioScaleFactor = carbohydrateRatioScaleFactor,
    insulinSensitivityScaleFactor = insulinSensitivityScaleFactor,
)

private fun PumpSettingsDataDto.UnitsDto.toDomain() = PumpSettingsData.Units(
    bloodGlucose = bloodGlucose?.toDomain(),
    carbohydrate = carbohydrate?.toDomain(),
    insulin = insulin?.toDomain(),
)

private fun PumpSettingsData.Units.toDto() = PumpSettingsDataDto.UnitsDto(
    bloodGlucose = bloodGlucose?.toDto(),
    carbohydrate = carbohydrate?.toDto(),
    insulin = insulin?.toDto(),
)

private fun PumpSettingsDataDto.PayloadDto.toDomain() = PumpSettingsData.Payload(syncIdentifier = syncIdentifier)

private fun PumpSettingsData.Payload.toDto() = PumpSettingsDataDto.PayloadDto(syncIdentifier = syncIdentifier)
