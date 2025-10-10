package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.prescription.NewPrescriptionDto
import org.tidepool.sdk.dto.prescription.PrescriptionDto
import org.tidepool.sdk.dto.prescription.UpdatePrescriptionDto

interface PrescriptionRepository {
    
    suspend fun createPrescription(
        sessionToken: String,
        newPrescription: NewPrescriptionDto,
    ): Result<PrescriptionDto>
    
    suspend fun getPrescription(
        sessionToken: String,
        prescriptionId: String,
    ): Result<PrescriptionDto>
    
    suspend fun updatePrescription(
        sessionToken: String,
        prescriptionId: String,
        updatePrescription: UpdatePrescriptionDto,
    ): Result<PrescriptionDto>
    
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
    ): Result<List<PrescriptionDto>>
    
    suspend fun getPrescriptionsByPrescriber(
        sessionToken: String,
        prescriberId: String,
        status: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<PrescriptionDto>>
    
    suspend fun getPrescriptionsForClinic(
        sessionToken: String,
        clinicId: String,
        status: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Result<List<PrescriptionDto>>
}