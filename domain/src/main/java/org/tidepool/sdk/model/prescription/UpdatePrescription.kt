package org.tidepool.sdk.model.prescription

data class UpdatePrescription(
    val medicationName: String? = null,
    val dosage: String? = null,
    val frequency: String? = null,
    val instructions: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val status: PrescriptionStatus? = null,
    val notes: String? = null,
)