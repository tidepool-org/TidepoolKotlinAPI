package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.dto.prescription.NewPrescriptionDto
import org.tidepool.sdk.dto.prescription.UpdatePrescriptionDto
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.prescription.Prescription
import org.tidepool.sdk.model.prescription.PrescriptionStatus
import org.tidepool.sdk.model.prescription.toDomain
import org.tidepool.sdk.model.prescription.toDto
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
        newPrescription = NewPrescriptionDto(
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
    ).map { it.toDomain() }
    
    // Get prescription by ID
    suspend fun getPrescription(
        prescriptionId: String,
    ): Result<Prescription> = prescriptionRepository.getPrescription(
        sessionToken = tokenProvider.getToken(),
        prescriptionId = prescriptionId,
    ).map { it.toDomain() }
    
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
        updatePrescription = UpdatePrescriptionDto(
            medicationName = medicationName,
            dosage = dosage,
            frequency = frequency,
            instructions = instructions,
            startDate = startDate?.toString(),
            endDate = endDate?.toString(),
            status = status?.toDto(),
            notes = notes,
        ),
    ).map { it.toDomain() }
    
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
        status = status?.toDto()?.name?.lowercase(),
        limit = limit,
        offset = offset,
    ).mapList { dto -> dto.toDomain() }
    
    // Get prescriptions by prescriber
    suspend fun getPrescriptionsByPrescriber(
        prescriberId: String,
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = prescriptionRepository.getPrescriptionsByPrescriber(
        sessionToken = tokenProvider.getToken(),
        prescriberId = prescriberId,
        status = status?.toDto()?.name?.lowercase(),
        limit = limit,
        offset = offset,
    ).mapList { dto -> dto.toDomain() }
    
    // Get prescriptions for a clinic
    suspend fun getPrescriptionsForClinic(
        clinicId: String,
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = prescriptionRepository.getPrescriptionsForClinic(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
        status = status?.toDto()?.name?.lowercase(),
        limit = limit,
        offset = offset,
    ).mapList { dto -> dto.toDomain() }
    
    // Get my prescriptions (as a patient)
    suspend fun getMyPrescriptions(
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            prescriptionRepository.getPrescriptionsForPatient(
                sessionToken = token,
                patientId = user.userId,
                status = status?.toDto()?.name?.lowercase(),
                limit = limit,
                offset = offset,
            )
        }
    }.mapList { dto -> dto.toDomain() }
    
    // Get prescriptions I've prescribed (as a prescriber)
    suspend fun getMyPrescribedPrescriptions(
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            prescriptionRepository.getPrescriptionsByPrescriber(
                sessionToken = token,
                prescriberId = user.userId,
                status = status?.toDto()?.name?.lowercase(),
                limit = limit,
                offset = offset,
            )
        }
    }.mapList { dto -> dto.toDomain() }
}