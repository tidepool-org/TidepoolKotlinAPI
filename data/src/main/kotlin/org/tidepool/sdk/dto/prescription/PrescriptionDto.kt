package org.tidepool.sdk.dto.prescription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
)