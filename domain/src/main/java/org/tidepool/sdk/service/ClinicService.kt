package org.tidepool.sdk.service

import org.tidepool.sdk.model.clinic.Clinic
import org.tidepool.sdk.model.clinic.Clinician
import org.tidepool.sdk.model.clinic.Patient
import org.tidepool.sdk.repository.ClinicRepository

class ClinicService(
    private val clinicRepository: ClinicRepository
) {
    
    suspend fun listClinics(
        sessionToken: String,
        limit: Int? = 100,
        offset: Int? = 0
    ): Result<List<Clinic>> = clinicRepository.listClinics(
        sessionToken = sessionToken,
        limit = limit,
        offset = offset
    )
    
    suspend fun getClinic(
        sessionToken: String,
        clinicId: String
    ): Result<Clinic> = clinicRepository.getClinic(sessionToken, clinicId)
    
    suspend fun findClinicByShareCode(
        sessionToken: String,
        shareCode: String
    ): Result<Clinic> = clinicRepository.getClinicByShareCode(sessionToken, shareCode)
    
    suspend fun createClinic(
        sessionToken: String,
        clinic: Clinic
    ): Result<Clinic> = clinicRepository.createClinic(sessionToken, clinic)
    
    suspend fun updateClinic(
        sessionToken: String,
        clinicId: String,
        clinic: Clinic
    ): Result<Clinic> = clinicRepository.updateClinic(sessionToken, clinicId, clinic)
    
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
    ): Result<List<Clinician>> = clinicRepository.getClinicClinicians(
        sessionToken = sessionToken,
        clinicId = clinicId,
        search = search,
        limit = limit,
        offset = offset
    )
    
    suspend fun addClinicianToClinic(
        sessionToken: String,
        clinicId: String,
        clinician: Clinician
    ): Result<Clinician> =
        clinicRepository.addClinicianToClinic(sessionToken, clinicId, clinician)
    
    suspend fun getClinicPatients(
        sessionToken: String,
        clinicId: String,
        search: String? = null,
        limit: Int? = 100,
        offset: Int? = 0,
        sort: String? = null
    ): Result<List<Patient>> = clinicRepository.getClinicPatients(
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
    ): Result<Patient> = clinicRepository.getClinicPatient(sessionToken, clinicId, patientId)
    
    suspend fun updateClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String,
        patient: Patient
    ): Result<Patient> =
        clinicRepository.updateClinicPatient(sessionToken, clinicId, patientId, patient)
    
    suspend fun removePatientFromClinic(
        sessionToken: String,
        clinicId: String,
        patientId: String
    ): Result<Unit> = clinicRepository.removePatientFromClinic(sessionToken, clinicId, patientId)
}