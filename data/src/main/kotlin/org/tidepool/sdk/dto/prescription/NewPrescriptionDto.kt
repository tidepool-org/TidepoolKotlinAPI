package org.tidepool.sdk.dto.prescription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewPrescriptionDto(
    @SerialName("clinicId")
    val clinicId: String? = null,
    @SerialName("patientId")
    val patientId: String,
    @SerialName("medicationName")
    val medicationName: String,
    @SerialName("dosage")
    val dosage: String,
    @SerialName("frequency")
    val frequency: String,
    @SerialName("instructions")
    val instructions: String? = null,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String? = null,
    @SerialName("notes")
    val notes: String? = null,
)