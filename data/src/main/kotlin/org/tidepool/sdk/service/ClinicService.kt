package org.tidepool.sdk.service

import org.tidepool.sdk.dto.clinic.ClinicDto
import org.tidepool.sdk.dto.clinic.ClinicianDto
import org.tidepool.sdk.dto.clinic.PatientDto
import org.tidepool.sdk.repository.ClinicRepository
import java.time.Instant

class ClinicService(
    private val clinicRepository: ClinicRepository
) {
    
    suspend fun listClinics(
        sessionToken: String,
        limit: Int? = 100,
        offset: Int? = 0
    ): Result<List<ClinicDto>> = clinicRepository.listClinics(
        sessionToken = sessionToken,
        limit = limit,
        offset = offset
    )
    
    suspend fun getClinic(
        sessionToken: String,
        clinicId: String
    ): Result<ClinicDto> = clinicRepository.getClinic(sessionToken, clinicId)
    
    suspend fun findClinicByShareCode(
        sessionToken: String,
        shareCode: String
    ): Result<ClinicDto> = clinicRepository.getClinicByShareCode(sessionToken, shareCode)
    
    suspend fun createClinic(
        sessionToken: String,
        clinic: ClinicDto
    ): Result<ClinicDto> = clinicRepository.createClinic(sessionToken, clinic)
    
    suspend fun updateClinic(
        sessionToken: String,
        clinicId: String,
        clinic: ClinicDto
    ): Result<ClinicDto> = clinicRepository.updateClinic(sessionToken, clinicId, clinic)
    
    suspend fun deleteClinic(
        sessionToken: String,
        clinicId: String
    ): Result<Unit> = clinicRepository.deleteClinic(sessionToken, clinicId)
    
    suspend fun getClinicClinicians(
        sessionToken: String,
        clinicId: String,
        search: String? = null,
        limit: Int? = 100,
        offset: Int? = 0
    ): Result<List<ClinicianDto>> = clinicRepository.getClinicClinicians(
        sessionToken = sessionToken,
        clinicId = clinicId,
        search = search,
        limit = limit,
        offset = offset
    )
    
    suspend fun addClinicianToClinic(
        sessionToken: String,
        clinicId: String,
        clinician: ClinicianDto
    ): Result<ClinicianDto> =
        clinicRepository.addClinicianToClinic(sessionToken, clinicId, clinician)
    
    suspend fun getClinicPatients(
        sessionToken: String,
        clinicId: String,
        search: String? = null,
        limit: Int? = 100,
        offset: Int? = 0,
        sort: String? = null
    ): Result<List<PatientDto>> = clinicRepository.getClinicPatients(
        sessionToken = sessionToken,
        clinicId = clinicId,
        search = search,
        limit = limit,
        offset = offset,
        sort = sort
    )
    
    suspend fun getClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String
    ): Result<PatientDto> = clinicRepository.getClinicPatient(sessionToken, clinicId, patientId)
    
    suspend fun updateClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String,
        patient: PatientDto
    ): Result<PatientDto> =
        clinicRepository.updateClinicPatient(sessionToken, clinicId, patientId, patient)
    
    suspend fun removePatientFromClinic(
        sessionToken: String,
        clinicId: String,
        patientId: String
    ): Result<Unit> = clinicRepository.removePatientFromClinic(sessionToken, clinicId, patientId)
}