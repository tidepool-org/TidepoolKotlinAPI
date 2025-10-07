package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.clinic.ClinicDto
import org.tidepool.sdk.dto.clinic.ClinicianDto
import org.tidepool.sdk.dto.clinic.PatientDto
import java.time.Instant

interface ClinicRepository {
    
    suspend fun listClinics(
        sessionToken: String,
        limit: Int? = null,
        offset: Int? = null,
        shareCode: String? = null,
        createdTimeStart: Instant? = null,
        createdTimeEnd: Instant? = null,
        ehrEnabled: Boolean? = null,
    ): Result<List<ClinicDto>>
    
    suspend fun createClinic(
        sessionToken: String,
        clinic: ClinicDto,
    ): Result<ClinicDto>
    
    suspend fun getClinicByShareCode(
        sessionToken: String,
        shareCode: String,
    ): Result<ClinicDto>
    
    suspend fun getClinic(
        sessionToken: String,
        clinicId: String,
    ): Result<ClinicDto>
    
    suspend fun updateClinic(
        sessionToken: String,
        clinicId: String,
        clinic: ClinicDto,
    ): Result<ClinicDto>
    
    suspend fun deleteClinic(
        sessionToken: String,
        clinicId: String,
    ): Result<Unit>
    
    // Clinician methods
    suspend fun getClinicClinicians(
        sessionToken: String,
        clinicId: String,
        search: String? = null,
        offset: Int? = null,
        limit: Int? = null,
        email: String? = null,
        role: String? = null,
    ): Result<List<ClinicianDto>>
    
    suspend fun addClinicianToClinic(
        sessionToken: String,
        clinicId: String,
        clinician: ClinicianDto,
    ): Result<ClinicianDto>
    
    // Patient methods
    suspend fun getClinicPatients(
        sessionToken: String,
        clinicId: String,
        search: String? = null,
        offset: Int? = null,
        limit: Int? = null,
        sort: String? = null,
    ): Result<List<PatientDto>>
    
    suspend fun getClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String,
    ): Result<PatientDto>
    
    suspend fun updateClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String,
        patient: PatientDto,
    ): Result<PatientDto>
    
    suspend fun removePatientFromClinic(
        sessionToken: String,
        clinicId: String,
        patientId: String,
    ): Result<Unit>
}