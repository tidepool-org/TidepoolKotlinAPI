package org.tidepool.sdk.repository

import org.tidepool.sdk.api.ClinicApi
import org.tidepool.sdk.dto.clinic.ClinicianDto
import org.tidepool.sdk.dto.clinic.toDomain
import org.tidepool.sdk.dto.clinic.toDto
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.clinic.Clinic
import org.tidepool.sdk.model.clinic.Clinician
import org.tidepool.sdk.model.clinic.Patient
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
    ): Result<List<Clinic>> = runCatchingNetworkExceptions {
        clinicApi.listClinics(
            sessionToken = sessionToken,
            limit = limit,
            offset = offset,
            shareCode = shareCode,
            createdTimeStart = createdTimeStart,
            createdTimeEnd = createdTimeEnd,
            ehrEnabled = ehrEnabled,
        )
    }.mapList { it.toDomain() }
    
    override suspend fun createClinic(
        sessionToken: String,
        clinic: Clinic
    ): Result<Clinic> = runCatchingNetworkExceptions {
        clinicApi.createClinic(
            sessionToken = sessionToken,
            clinic = clinic.toDto(),
        )
    }.map { it.toDomain() }
    
    override suspend fun getClinicByShareCode(
        sessionToken: String,
        shareCode: String
    ): Result<Clinic> = runCatchingNetworkExceptions {
        clinicApi.getClinicByShareCode(sessionToken, shareCode)
    }.map { it.toDomain() }
    
    override suspend fun getClinic(
        sessionToken: String,
        clinicId: String
    ): Result<Clinic> = runCatchingNetworkExceptions {
        clinicApi.getClinic(sessionToken, clinicId)
    }.map { it.toDomain() }
    
    override suspend fun updateClinic(
        sessionToken: String,
        clinicId: String,
        clinic: Clinic
    ): Result<Clinic> = runCatchingNetworkExceptions {
        clinicApi.updateClinic(
            sessionToken = sessionToken,
            clinicId = clinicId,
            clinic = clinic.toDto(),
        )
    }.map { it.toDomain() }
    
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
    ): Result<List<Clinician>> = runCatchingNetworkExceptions {
        clinicApi.getClinicClinicians(
            sessionToken = sessionToken,
            clinicId = clinicId,
            search = search,
            offset = offset,
            limit = limit,
            email = email,
            role = role
        )
    }.mapList { it.toDomain() }
    
    override suspend fun addClinicianToClinic(
        sessionToken: String,
        clinicId: String,
        clinician: Clinician
    ): Result<Clinician> = runCatchingNetworkExceptions {
        clinicApi.addClinicianToClinic(
            sessionToken = sessionToken,
            clinicId = clinicId,
            clinician = clinician.toDto(),
        )
    }.map { it.toDomain() }
    
    // Patient methods
    override suspend fun getClinicPatients(
        sessionToken: String,
        clinicId: String,
        search: String?,
        offset: Int?,
        limit: Int?,
        sort: String?
    ): Result<List<Patient>> = runCatchingNetworkExceptions {
        clinicApi.getClinicPatients(
            sessionToken = sessionToken,
            clinicId = clinicId,
            search = search,
            offset = offset,
            limit = limit,
            sort = sort
        )
    }.mapList { it.toDomain() }
    
    override suspend fun getClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String
    ): Result<Patient> = runCatchingNetworkExceptions {
        clinicApi.getClinicPatient(sessionToken, clinicId, patientId)
    }.map { it.toDomain() }
    
    override suspend fun updateClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String,
        patient: Patient
    ): Result<Patient> = runCatchingNetworkExceptions {
        clinicApi.updateClinicPatient(
            sessionToken = sessionToken,
            clinicId = clinicId,
            patientId = patientId,
            patient = patient.toDto()
        )
    }.map { it.toDomain() }
    
    override suspend fun removePatientFromClinic(
        sessionToken: String,
        clinicId: String,
        patientId: String
    ): Result<Unit> = runCatchingNetworkExceptions {
        clinicApi.removePatientFromClinic(sessionToken, clinicId, patientId)
    }
}