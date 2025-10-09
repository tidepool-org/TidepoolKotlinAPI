package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.ClinicApi
import org.tidepool.sdk.dto.clinic.ClinicDto
import org.tidepool.sdk.dto.clinic.ClinicianDto
import org.tidepool.sdk.dto.clinic.PatientDto
import org.tidepool.sdk.repository.ClinicRepository
import org.tidepool.sdk.runCatchingNetworkExceptions
import java.time.Instant

class ClinicRepositoryImpl(
    private val clinicApi: ClinicApi,
) : ClinicRepository {
    
    override suspend fun listClinics(
        sessionToken: String,
        limit: Int?,
        offset: Int?,
        shareCode: String?,
        createdTimeStart: Instant?,
        createdTimeEnd: Instant?,
        ehrEnabled: Boolean?
    ): Result<List<ClinicDto>> = runCatchingNetworkExceptions {
        clinicApi.listClinics(
            sessionToken = sessionToken,
            limit = limit,
            offset = offset,
            shareCode = shareCode,
            createdTimeStart = createdTimeStart,
            createdTimeEnd = createdTimeEnd,
            ehrEnabled = ehrEnabled,
        )
    }
    
    override suspend fun createClinic(
        sessionToken: String,
        clinic: ClinicDto
    ): Result<ClinicDto> = runCatchingNetworkExceptions {
        clinicApi.createClinic(sessionToken, clinic)
    }
    
    override suspend fun getClinicByShareCode(
        sessionToken: String,
        shareCode: String
    ): Result<ClinicDto> = runCatchingNetworkExceptions {
        clinicApi.getClinicByShareCode(sessionToken, shareCode)
    }
    
    override suspend fun getClinic(
        sessionToken: String,
        clinicId: String
    ): Result<ClinicDto> = runCatchingNetworkExceptions {
        clinicApi.getClinic(sessionToken, clinicId)
    }
    
    override suspend fun updateClinic(
        sessionToken: String,
        clinicId: String,
        clinic: ClinicDto
    ): Result<ClinicDto> = runCatchingNetworkExceptions {
        clinicApi.updateClinic(sessionToken, clinicId, clinic)
    }
    
    override suspend fun deleteClinic(
        sessionToken: String,
        clinicId: String
    ): Result<Unit> = runCatchingNetworkExceptions {
        clinicApi.deleteClinic(sessionToken, clinicId)
    }
    
    // Clinician methods
    override suspend fun getClinicClinicians(
        sessionToken: String,
        clinicId: String,
        search: String?,
        offset: Int?,
        limit: Int?,
        email: String?,
        role: String?
    ): Result<List<ClinicianDto>> = runCatchingNetworkExceptions {
        clinicApi.getClinicClinicians(
            sessionToken = sessionToken,
            clinicId = clinicId,
            search = search,
            offset = offset,
            limit = limit,
            email = email,
            role = role
        )
    }
    
    override suspend fun addClinicianToClinic(
        sessionToken: String,
        clinicId: String,
        clinician: ClinicianDto
    ): Result<ClinicianDto> = runCatchingNetworkExceptions {
        clinicApi.addClinicianToClinic(sessionToken, clinicId, clinician)
    }
    
    // Patient methods
    override suspend fun getClinicPatients(
        sessionToken: String,
        clinicId: String,
        search: String?,
        offset: Int?,
        limit: Int?,
        sort: String?
    ): Result<List<PatientDto>> = runCatchingNetworkExceptions {
        clinicApi.getClinicPatients(
            sessionToken = sessionToken,
            clinicId = clinicId,
            search = search,
            offset = offset,
            limit = limit,
            sort = sort
        )
    }
    
    override suspend fun getClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String
    ): Result<PatientDto> = runCatchingNetworkExceptions {
        clinicApi.getClinicPatient(sessionToken, clinicId, patientId)
    }
    
    override suspend fun updateClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String,
        patient: PatientDto
    ): Result<PatientDto> = runCatchingNetworkExceptions {
        clinicApi.updateClinicPatient(sessionToken, clinicId, patientId, patient)
    }
    
    override suspend fun removePatientFromClinic(
        sessionToken: String,
        clinicId: String,
        patientId: String
    ): Result<Unit> = runCatchingNetworkExceptions {
        clinicApi.removePatientFromClinic(sessionToken, clinicId, patientId)
    }
}