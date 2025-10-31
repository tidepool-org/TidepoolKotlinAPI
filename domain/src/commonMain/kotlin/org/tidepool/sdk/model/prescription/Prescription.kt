package org.tidepool.sdk.model.prescription

import java.time.Instant

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
)