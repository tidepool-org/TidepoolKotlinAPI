package org.tidepool.sdk.model.prescription

import kotlinx.datetime.Instant

data class Prescription(
    val id: String,
    val userId: String,
    val clinicId: String?,
    val prescriberId: String,
    val patientId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val instructions: String?,
    val startDate: Instant,
    val endDate: Instant?,
    val status: PrescriptionStatus,
    val createdTime: Instant,
    val modifiedTime: Instant,
    val notes: String?,
    val initialSettings: InitialSettings? = null,
)

data class InitialSettings(
    val glucoseUnit: String?,
    val glucoseSafetyLimit: Double?,
    val glucoseTargetSchedule: List<GlucoseTarget>,
    val basalRateSchedule: List<BasalRate>,
    val carbRatioSchedule: List<CarbRatio>,
    val insulinSensitivitySchedule: List<ISF>,
    val maxBasalRate: Double?,
    val maxBolus: Double?,
    val cgmId: String?,
    val pumpId: String?
)

data class GlucoseTarget(val startSeconds: Long, val low: Double, val high: Double)
data class BasalRate(val startSeconds: Long, val rate: Double)
data class CarbRatio(val startSeconds: Long, val ratio: Double)
data class ISF(val startSeconds: Long, val amount: Double)