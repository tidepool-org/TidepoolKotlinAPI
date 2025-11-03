package org.tidepool.sdk.repository

import org.tidepool.sdk.model.clinic.Clinic
import org.tidepool.sdk.model.clinic.Clinician
import org.tidepool.sdk.model.clinic.Patient
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
    ): Result<List<Clinic>>
    
    suspend fun createClinic(
        sessionToken: String,
        clinic: Clinic,
    ): Result<Clinic>
    
    suspend fun getClinicByShareCode(
        sessionToken: String,
        shareCode: String,
    ): Result<Clinic>
    
    suspend fun getClinic(
        sessionToken: String,
        clinicId: String,
    ): Result<Clinic>
    
    suspend fun updateClinic(
        sessionToken: String,
        clinicId: String,
        clinic: Clinic,
    ): Result<Clinic>
    
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
    ): Result<List<Clinician>>
    
    suspend fun addClinicianToClinic(
        sessionToken: String,
        clinicId: String,
        clinician: Clinician,
    ): Result<Clinician>
    
    // Patient methods
    suspend fun getClinicPatients(
        sessionToken: String,
        clinicId: String,
        search: String? = null,
        offset: Int? = null,
        limit: Int? = null,
        sort: String? = null,
    ): Result<List<Patient>>
    
    suspend fun getClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String,
    ): Result<Patient>
    
    suspend fun updateClinicPatient(
        sessionToken: String,
        clinicId: String,
        patientId: String,
        patient: Patient,
    ): Result<Patient>
    
    suspend fun removePatientFromClinic(
        sessionToken: String,
        clinicId: String,
        patientId: String,
    ): Result<Unit>
}