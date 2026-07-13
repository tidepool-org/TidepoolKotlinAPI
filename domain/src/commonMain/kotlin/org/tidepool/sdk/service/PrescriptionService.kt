package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.model.prescription.ClaimedPrescription
import org.tidepool.sdk.model.prescription.InitialSettings
import org.tidepool.sdk.model.prescription.NewPrescription
import org.tidepool.sdk.model.prescription.Prescription
import org.tidepool.sdk.model.prescription.PrescriptionStatus
import org.tidepool.sdk.model.prescription.UpdatePrescription
import org.tidepool.sdk.repository.PrescriptionRepository
import org.tidepool.sdk.repository.UserRepository
import kotlinx.datetime.Instant

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
    ): Result<Prescription> = tokenProvider.getToken().flatMap {
        prescriptionRepository.createPrescription(
            sessionToken = it,
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
    }

    // Get prescription by ID
    suspend fun getPrescription(
        prescriptionId: String,
    ): Result<Prescription> = tokenProvider.getToken().flatMap {
        prescriptionRepository.getPrescription(
            sessionToken = it,
            prescriptionId = prescriptionId,
        )
    }

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
    ): Result<Prescription> = tokenProvider.getToken().flatMap {
        prescriptionRepository.updatePrescription(
            sessionToken = it,
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
    }

    // Delete prescription
    suspend fun deletePrescription(
        prescriptionId: String,
    ): Result<Unit> = tokenProvider.getToken().flatMap {
        prescriptionRepository.deletePrescription(
            sessionToken = it,
            prescriptionId = prescriptionId,
        )
    }

    // Get prescriptions for a patient
    suspend fun getPrescriptionsForPatient(
        patientId: String,
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = tokenProvider.getToken().flatMap {
        prescriptionRepository.getPrescriptionsForPatient(
            sessionToken = it,
            patientId = patientId,
            status = status?.name?.lowercase(),
            limit = limit,
            offset = offset,
        )
    }

    // Get prescriptions by prescriber
    suspend fun getPrescriptionsByPrescriber(
        prescriberId: String,
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = tokenProvider.getToken().flatMap {
        prescriptionRepository.getPrescriptionsByPrescriber(
            sessionToken = it,
            prescriberId = prescriberId,
            status = status?.name?.lowercase(),
            limit = limit,
            offset = offset,
        )
    }

    // Get prescriptions for a clinic
    suspend fun getPrescriptionsForClinic(
        clinicId: String,
        status: PrescriptionStatus? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>> = tokenProvider.getToken().flatMap {
        prescriptionRepository.getPrescriptionsForClinic(
            sessionToken = it,
            clinicId = clinicId,
            status = status?.name?.lowercase(),
            limit = limit,
            offset = offset,
        )
    }

    // Claim a prescription for the current patient
    // userId must be obtained from sdk.users.getCurrentUser() by the caller
    suspend fun claimPrescription(
        userId: String,
        accessCode: String,
        birthday: String,
    ): Result<InitialSettings?> = tokenProvider.getToken().flatMap {
        prescriptionRepository.claimPrescription(
            sessionToken = it,
            userId = userId,
            accessCode = accessCode,
            birthday = birthday,
        )
    }

    // Latest prescription already on the patient's account (null if none).
    // Mirrors iOS checkAccountForExistingPrescription(): used to skip the access-code screen.
    suspend fun getLatestPrescription(
        userId: String,
    ): Result<ClaimedPrescription?> = tokenProvider.getToken().flatMap {
        prescriptionRepository.getLatestPrescription(
            sessionToken = it,
            userId = userId,
        )
    }
}