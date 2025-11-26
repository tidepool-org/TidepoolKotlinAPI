package org.tidepool.sdk.dto.prescription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
    notes = notes
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