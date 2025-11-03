package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.model.prescription.NewPrescription
import org.tidepool.sdk.model.prescription.Prescription
import org.tidepool.sdk.model.prescription.PrescriptionStatus
import org.tidepool.sdk.model.prescription.UpdatePrescription
import org.tidepool.sdk.repository.PrescriptionRepository
import org.tidepool.sdk.repository.UserRepository
import java.time.Instant

class PrescriptionService internal constructor(
    private val prescriptionRepository: PrescriptionRepository,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) {
    
    // Create a new prescription
    suspend fun createPrescription(
        clinicId: String?,
        patientId: String,
        medicationName: String,
        dosage: String,
        frequency: String,
        instructions: String?,
        startDate: Instant,
        endDate: Instant?,
        notes: String?,
    ): Result<Prescription> = prescriptionRepository.createPrescription(
        sessionToken = tokenProvider.getToken(),
        newPrescription = NewPrescription(
            clinicId = clinicId,
            patientId = patientId,
            medicationName = medicationName,
            dosage = dosage,
            frequency = frequency,
            instructions = instructions,
            startDate = startDate.toString(),
            endDate = endDate?.toString(),
            notes = notes,
        ),
    )
    
    // Get prescription by ID
    suspend fun getPrescription(
        prescriptionId: String,
    ): Result<Prescription> = prescriptionRepository.getPrescription(
        sessionToken = tokenProvider.getToken(),
        prescriptionId = prescriptionId,
    )
    
    // Update prescription
    suspend fun updatePrescription(
        prescriptionId: String,
        medicationName: String? = null,
        dosage: String? = null,
        frequency: String? = null,
        instructions: String? = null,
        startDate: Instant? = null,
        endDate: Instant? = null,
        status: PrescriptionStatus? = null,
        notes: String? = null,
    ): Result<Prescription> = prescriptionRepository.updatePrescription(
        sessionToken = tokenProvider.getToken(),
        prescriptionId = prescriptionId,
        updatePrescription = UpdatePrescription(
            medicationName = medicationName,
            dosage = dosage,
            frequency = frequency,
            instructions = instructions,
            startDate = startDate?.toString(),
            endDate = endDate?.toString(),
            status = status,
            notes = notes,
        ),
    )
    
    // Delete prescription
    suspend fun deletePrescription(
        prescriptionId: String,
    ): Result<Unit> = prescriptionRepository.deletePrescription(
        sessionToken = tokenProvider.getToken(),
        prescriptionId = prescriptionId,
    )
    
    // Get prescriptions for a patient
    suspend fun getPrescriptionsForPatient(
        patientId: String,
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = prescriptionRepository.getPrescriptionsForPatient(
        sessionToken = tokenProvider.getToken(),
        patientId = patientId,
        status = status?.name?.lowercase(),
        limit = limit,
        offset = offset,
    )
    
    // Get prescriptions by prescriber
    suspend fun getPrescriptionsByPrescriber(
        prescriberId: String,
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = prescriptionRepository.getPrescriptionsByPrescriber(
        sessionToken = tokenProvider.getToken(),
        prescriberId = prescriberId,
        status = status?.name?.lowercase(),
        limit = limit,
        offset = offset,
    )
    
    // Get prescriptions for a clinic
    suspend fun getPrescriptionsForClinic(
        clinicId: String,
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = prescriptionRepository.getPrescriptionsForClinic(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
        status = status?.name?.lowercase(),
        limit = limit,
        offset = offset,
    )
}