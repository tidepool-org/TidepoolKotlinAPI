package org.tidepool.sdk.dto.prescription

import io.mcarle.konvert.api.KonvertFrom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.prescription.UpdatePrescription

@Serializable
data class UpdatePrescriptionDto(
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
    @SerialName("notes")
    val notes: String? = null,
) {
    @KonvertFrom(UpdatePrescription::class, mapFunctionName = "fromDomain")
    companion object {}
}