package org.tidepool.sdk.model.prescription

data class NewPrescription(
    val clinicId: String? = null,
    val patientId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val instructions: String? = null,
    val startDate: String,
    val endDate: String? = null,
    val notes: String? = null,
)