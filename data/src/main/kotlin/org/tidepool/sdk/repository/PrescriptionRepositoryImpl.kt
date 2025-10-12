package org.tidepool.sdk.repository

import org.tidepool.sdk.api.PrescriptionApi
import org.tidepool.sdk.dto.prescription.NewPrescriptionDto
import org.tidepool.sdk.dto.prescription.UpdatePrescriptionDto
import org.tidepool.sdk.dto.prescription.fromDomain
import org.tidepool.sdk.dto.prescription.toDomain
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.prescription.NewPrescription
import org.tidepool.sdk.model.prescription.Prescription
import org.tidepool.sdk.model.prescription.UpdatePrescription
import org.tidepool.sdk.runCatchingNetworkExceptions

class PrescriptionRepositoryImpl(
    private val prescriptionApi: PrescriptionApi,
) : PrescriptionRepository {
    
    override suspend fun createPrescription(
        sessionToken: String,
        newPrescription: NewPrescription,
    ): Result<Prescription> = runCatchingNetworkExceptions {
        prescriptionApi.createPrescription(
            sessionToken = sessionToken,
            requestBody = NewPrescriptionDto.fromDomain(newPrescription),
        )
    }.map { it.toDomain() }
    
    override suspend fun getPrescription(
        sessionToken: String,
        prescriptionId: String,
    ): Result<Prescription> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescription(
            sessionToken = sessionToken,
            prescriptionId = prescriptionId,
        )
    }.map { it.toDomain() }
    
    override suspend fun updatePrescription(
        sessionToken: String,
        prescriptionId: String,
        updatePrescription: UpdatePrescription,
    ): Result<Prescription> = runCatchingNetworkExceptions {
        prescriptionApi.updatePrescription(
            sessionToken = sessionToken,
            prescriptionId = prescriptionId,
            requestBody = UpdatePrescriptionDto.fromDomain(updatePrescription),
        )
    }.map { it.toDomain() }
    
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
    ): Result<List<Prescription>> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescriptionsForPatient(
            sessionToken = sessionToken,
            patientId = patientId,
            status = status,
            limit = limit,
            offset = offset,
        )
    }.mapList { it.toDomain() }
    
    override suspend fun getPrescriptionsByPrescriber(
        sessionToken: String,
        prescriberId: String,
        status: String?,
        limit: Int?,
        offset: Int?,
    ): Result<List<Prescription>> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescriptionsByPrescriber(
            sessionToken = sessionToken,
            prescriberId = prescriberId,
            status = status,
            limit = limit,
            offset = offset,
        )
    }.mapList { it.toDomain() }
    
    override suspend fun getPrescriptionsForClinic(
        sessionToken: String,
        clinicId: String,
        status: String?,
        limit: Int?,
        offset: Int?,
    ): Result<List<Prescription>> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescriptionsForClinic(
            sessionToken = sessionToken,
            clinicId = clinicId,
            status = status,
            limit = limit,
            offset = offset,
        )
    }.mapList { it.toDomain() }
}