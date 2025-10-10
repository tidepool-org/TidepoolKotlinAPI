package org.tidepool.sdk.model.prescription

import org.tidepool.sdk.dto.prescription.PrescriptionDto
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

fun PrescriptionDto.toDomain(): Prescription = Prescription(
    id = id ?: throw IllegalArgumentException("Prescription ID cannot be null"),
    userId = userId ?: throw IllegalArgumentException("User ID cannot be null"),
    clinicId = clinicId,
    prescriberId = prescriberId ?: throw IllegalArgumentException("Prescriber ID cannot be null"),
    patientId = patientId ?: throw IllegalArgumentException("Patient ID cannot be null"),
    medicationName = medicationName
        ?: throw IllegalArgumentException("Medication name cannot be null"),
    dosage = dosage ?: throw IllegalArgumentException("Dosage cannot be null"),
    frequency = frequency ?: throw IllegalArgumentException("Frequency cannot be null"),
    instructions = instructions,
    startDate = startDate?.let { Instant.parse(it) }
        ?: throw IllegalArgumentException("Start date cannot be null"),
    endDate = endDate?.let { Instant.parse(it) },
    status = status?.toDomain() ?: throw IllegalArgumentException("Status cannot be null"),
    createdTime = createdTime?.let { Instant.parse(it) }
        ?: throw IllegalArgumentException("Created time cannot be null"),
    modifiedTime = modifiedTime?.let { Instant.parse(it) }
        ?: throw IllegalArgumentException("Modified time cannot be null"),
    notes = notes,
)