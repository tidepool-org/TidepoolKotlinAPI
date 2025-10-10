package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.PrescriptionApi
import org.tidepool.sdk.dto.prescription.NewPrescriptionDto
import org.tidepool.sdk.dto.prescription.PrescriptionDto
import org.tidepool.sdk.dto.prescription.UpdatePrescriptionDto
import org.tidepool.sdk.repository.PrescriptionRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

class PrescriptionRepositoryImpl(
    private val prescriptionApi: PrescriptionApi,
) : PrescriptionRepository {
    
    override suspend fun createPrescription(
        sessionToken: String,
        newPrescription: NewPrescriptionDto,
    ): Result<PrescriptionDto> = runCatchingNetworkExceptions {
        prescriptionApi.createPrescription(
            sessionToken = sessionToken,
            requestBody = newPrescription,
        )
    }
    
    override suspend fun getPrescription(
        sessionToken: String,
        prescriptionId: String,
    ): Result<PrescriptionDto> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescription(
            sessionToken = sessionToken,
            prescriptionId = prescriptionId,
        )
    }
    
    override suspend fun updatePrescription(
        sessionToken: String,
        prescriptionId: String,
        updatePrescription: UpdatePrescriptionDto,
    ): Result<PrescriptionDto> = runCatchingNetworkExceptions {
        prescriptionApi.updatePrescription(
            sessionToken = sessionToken,
            prescriptionId = prescriptionId,
            requestBody = updatePrescription,
        )
    }
    
    override suspend fun deletePrescription(
        sessionToken: String,
        prescriptionId: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        prescriptionApi.deletePrescription(
            sessionToken = sessionToken,
            prescriptionId = prescriptionId,
        )
    }
    
    override suspend fun getPrescriptionsForPatient(
        sessionToken: String,
        patientId: String,
        status: String?,
        limit: Int?,
        offset: Int?,
    ): Result<List<PrescriptionDto>> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescriptionsForPatient(
            sessionToken = sessionToken,
            patientId = patientId,
            status = status,
            limit = limit,
            offset = offset,
        )
    }
    
    override suspend fun getPrescriptionsByPrescriber(
        sessionToken: String,
        prescriberId: String,
        status: String?,
        limit: Int?,
        offset: Int?,
    ): Result<List<PrescriptionDto>> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescriptionsByPrescriber(
            sessionToken = sessionToken,
            prescriberId = prescriberId,
            status = status,
            limit = limit,
            offset = offset,
        )
    }
    
    override suspend fun getPrescriptionsForClinic(
        sessionToken: String,
        clinicId: String,
        status: String?,
        limit: Int?,
        offset: Int?,
    ): Result<List<PrescriptionDto>> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescriptionsForClinic(
            sessionToken = sessionToken,
            clinicId = clinicId,
            status = status,
            limit = limit,
            offset = offset,
        )
    }
}