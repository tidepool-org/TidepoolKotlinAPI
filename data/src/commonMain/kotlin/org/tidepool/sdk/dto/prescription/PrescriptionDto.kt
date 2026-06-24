package org.tidepool.sdk.dto.prescription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.prescription.BasalRate
import org.tidepool.sdk.model.prescription.CarbRatio
import org.tidepool.sdk.model.prescription.ClaimedPrescription
import org.tidepool.sdk.model.prescription.GlucoseTarget
import org.tidepool.sdk.model.prescription.ISF
import org.tidepool.sdk.model.prescription.InitialSettings
import org.tidepool.sdk.model.prescription.Prescription
import kotlinx.datetime.Instant

@Serializable
data class PrescriptionDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("userId")
    val userId: String? = null,
    @SerialName("clinicId")
    val clinicId: String? = null,
    @SerialName("prescriberId")
    val prescriberId: String? = null,
    @SerialName("patientId")
    val patientId: String? = null,
    @SerialName("medicationName")
    val medicationName: String? = null,
    @SerialName("dosage")
    val dosage: String? = null,
    @SerialName("frequency")
    val frequency: String? = null,
    @SerialName("instructions")
    val instructions: String? = null,
    @SerialName("startDate")
    val startDate: String? = null,
    @SerialName("endDate")
    val endDate: String? = null,
    @SerialName("status")
    val status: PrescriptionStatusDto? = null,
    @SerialName("createdTime")
    val createdTime: String? = null,
    @SerialName("modifiedTime")
    val modifiedTime: String? = null,
    @SerialName("notes")
    val notes: String? = null,
    @SerialName("latestRevision")
    val latestRevision: PrescriptionRevisionDto? = null,
)

// Manual mapping functions
internal fun PrescriptionDto.toDomain(): Prescription = Prescription(
    id = id!!,
    userId = userId!!,
    clinicId = clinicId,
    prescriberId = prescriberId!!,
    patientId = patientId!!,
    medicationName = medicationName!!,
    dosage = dosage!!,
    frequency = frequency!!,
    instructions = instructions,
    startDate = Instant.parse(startDate!!),
    endDate = endDate?.let { Instant.parse(it) },
    status = status!!.toDomain(),
    createdTime = Instant.parse(createdTime!!),
    modifiedTime = Instant.parse(modifiedTime!!),
    notes = notes,
    initialSettings = latestRevision?.attributes?.initialSettings?.let { s ->
        InitialSettings(
            glucoseUnit = s.glucoseUnit,
            glucoseSafetyLimit = s.glucoseSafetyLimit,
            glucoseTargetSchedule = s.glucoseTargetSchedule?.map { GlucoseTarget(it.startSeconds / 1000, it.low, it.high) } ?: emptyList(),
            basalRateSchedule = s.basalRateSchedule?.map { BasalRate(it.startSeconds / 1000, it.rate) } ?: emptyList(),
            carbRatioSchedule = s.carbRatioSchedule?.map { CarbRatio(it.startSeconds / 1000, it.ratio) } ?: emptyList(),
            insulinSensitivitySchedule = s.insulinSensitivitySchedule?.map { ISF(it.startSeconds / 1000, it.amount) } ?: emptyList(),
            maxBasalRate = s.basalRateMaximum?.value,
            maxBolus = s.bolusAmountMaximum?.value,
            cgmId = s.cgmId,
            pumpId = s.pumpId,
        )
    },
)

internal fun Prescription.toDto(): PrescriptionDto = PrescriptionDto(
    id = id,
    userId = userId,
    clinicId = clinicId,
    prescriberId = prescriberId,
    patientId = patientId,
    medicationName = medicationName,
    dosage = dosage,
    frequency = frequency,
    instructions = instructions,
    startDate = startDate.toString(),
    endDate = endDate?.toString(),
    status = status.toDto(),
    createdTime = createdTime.toString(),
    modifiedTime = modifiedTime.toString(),
    notes = notes
)

@Serializable
data class PrescriptionRevisionDto(
    @SerialName("attributes") val attributes: PrescriptionAttributesDto? = null
)

@Serializable
data class PrescriptionAttributesDto(
    @SerialName("initialSettings") val initialSettings: InitialSettingsDto? = null
)

@Serializable
data class InitialSettingsDto(
    @SerialName("bloodGlucoseUnits")             val glucoseUnit: String? = null,
    @SerialName("glucoseSafetyLimit")            val glucoseSafetyLimit: Double? = null,
    @SerialName("bloodGlucoseTargetSchedule")    val glucoseTargetSchedule: List<GlucoseTargetEntryDto>? = null,
    @SerialName("basalRateSchedule")             val basalRateSchedule: List<BasalRateEntryDto>? = null,
    @SerialName("carbohydrateRatioSchedule")     val carbRatioSchedule: List<CarbRatioEntryDto>? = null,
    @SerialName("insulinSensitivitySchedule")    val insulinSensitivitySchedule: List<ISFEntryDto>? = null,
    @SerialName("basalRateMaximum")              val basalRateMaximum: RateValueDto? = null,
    @SerialName("bolusAmountMaximum")            val bolusAmountMaximum: RateValueDto? = null,
    @SerialName("cgmId")                         val cgmId: String? = null,
    @SerialName("pumpId")                        val pumpId: String? = null
)

@Serializable
data class GlucoseTargetEntryDto(
    @SerialName("start") val startSeconds: Long = 0,
    @SerialName("low")   val low: Double = 0.0,
    @SerialName("high")  val high: Double = 0.0
)

@Serializable
data class BasalRateEntryDto(
    @SerialName("start") val startSeconds: Long = 0,
    @SerialName("rate")  val rate: Double = 0.0
)

@Serializable
data class CarbRatioEntryDto(
    @SerialName("start")  val startSeconds: Long = 0,
    @SerialName("amount") val ratio: Double = 0.0
)

@Serializable
data class ISFEntryDto(
    @SerialName("start")  val startSeconds: Long = 0,
    @SerialName("amount") val amount: Double = 0.0
)

@Serializable
data class RateValueDto(
    @SerialName("units") val units: String? = null,
    @SerialName("value") val value: Double = 0.0
)

@Serializable
data class ClaimedPrescriptionResponseDto(
    @SerialName("id")               val id: String? = null,
    @SerialName("patientUserId")    val patientUserId: String? = null,
    @SerialName("prescriberUserId") val prescriberUserId: String? = null,
    @SerialName("state")            val state: String? = null,
    @SerialName("createdTime")      val createdTime: String? = null,
    @SerialName("modifiedTime")     val modifiedTime: String? = null,
    @SerialName("submittedTime")    val submittedTime: String? = null,
    @SerialName("latestRevision")   val latestRevision: PrescriptionRevisionDto? = null,
)

internal fun ClaimedPrescriptionResponseDto.toClaimedPrescription(): ClaimedPrescription =
    ClaimedPrescription(
        initialSettings = toInitialSettings(),
        prescriberUserId = prescriberUserId,
        submittedDate = submittedTime ?: modifiedTime ?: createdTime,
        state = state,
    )

internal fun ClaimedPrescriptionResponseDto.toInitialSettings(): InitialSettings? {
    val s = latestRevision?.attributes?.initialSettings ?: return null
    return InitialSettings(
        glucoseUnit = s.glucoseUnit,
        glucoseSafetyLimit = s.glucoseSafetyLimit,
        glucoseTargetSchedule = s.glucoseTargetSchedule?.map { GlucoseTarget(it.startSeconds / 1000, it.low, it.high) } ?: emptyList(),
        basalRateSchedule = s.basalRateSchedule?.map { BasalRate(it.startSeconds / 1000, it.rate) } ?: emptyList(),
        carbRatioSchedule = s.carbRatioSchedule?.map { CarbRatio(it.startSeconds / 1000, it.ratio) } ?: emptyList(),
        insulinSensitivitySchedule = s.insulinSensitivitySchedule?.map { ISF(it.startSeconds / 1000, it.amount) } ?: emptyList(),
        maxBasalRate = s.basalRateMaximum?.value,
        maxBolus = s.bolusAmountMaximum?.value,
        cgmId = s.cgmId,
        pumpId = s.pumpId,
    )
}