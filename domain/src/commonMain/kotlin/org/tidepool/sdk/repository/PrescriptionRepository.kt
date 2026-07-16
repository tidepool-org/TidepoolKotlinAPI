package org.tidepool.sdk.repository

import org.tidepool.sdk.model.prescription.ClaimedPrescription
import org.tidepool.sdk.model.prescription.InitialSettings
import org.tidepool.sdk.model.prescription.NewPrescription
import org.tidepool.sdk.model.prescription.Prescription
import org.tidepool.sdk.model.prescription.UpdatePrescription

interface PrescriptionRepository {

    suspend fun createPrescription(
        sessionToken: String,
        newPrescription: NewPrescription,
    ): Result<Prescription>

    suspend fun getPrescription(
        sessionToken: String,
        prescriptionId: String,
    ): Result<Prescription>

    suspend fun updatePrescription(
        sessionToken: String,
        prescriptionId: String,
        updatePrescription: UpdatePrescription,
    ): Result<Prescription>

    suspend fun deletePrescription(
        sessionToken: String,
        prescriptionId: String,
    ): Result<Unit>

    suspend fun getPrescriptionsForPatient(
        sessionToken: String,
        patientId: String,
        status: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>>

    suspend fun getPrescriptionsByPrescriber(
        sessionToken: String,
        prescriberId: String,
        status: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>>

    suspend fun getPrescriptionsForClinic(
        sessionToken: String,
        clinicId: String,
        status: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<Prescription>>

    suspend fun claimPrescription(
        sessionToken: String,
        userId: String,
        accessCode: String,
        birthday: String,
    ): Result<InitialSettings?>

    suspend fun getLatestPrescription(
        sessionToken: String,
        userId: String,
    ): Result<ClaimedPrescription?>
}