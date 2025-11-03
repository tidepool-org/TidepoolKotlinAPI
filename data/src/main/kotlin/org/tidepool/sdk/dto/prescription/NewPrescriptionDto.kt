package org.tidepool.sdk.dto.prescription

import io.mcarle.konvert.api.KonvertFrom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.prescription.NewPrescription

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
) {
    @KonvertFrom(NewPrescription::class, mapFunctionName = "fromDomain")
    companion object {}
}