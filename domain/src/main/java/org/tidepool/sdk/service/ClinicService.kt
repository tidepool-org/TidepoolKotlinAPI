package org.tidepool.sdk.service

import org.tidepool.sdk.Paginator
import org.tidepool.sdk.PaginatorImpl
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.model.clinic.Clinic
import org.tidepool.sdk.model.clinic.Clinician
import org.tidepool.sdk.model.clinic.Patient
import org.tidepool.sdk.repository.ClinicRepository

class ClinicService(
    private val clinicRepository: ClinicRepository,
    private val tokenProvider: TokenProvider,
) {
    
    suspend fun getClinicsPaginator(
        pageSize: Int = 42,
        onPageLoadSuccess: suspend (List<Clinic>, Boolean) -> Unit,
        onPageLoadFailure: suspend (Throwable) -> Unit,
    ): Paginator<Int, List<Clinic>> = PaginatorImpl(
        initialKey = 0,
        onRequest = { offset: Int ->
            clinicRepository.listClinics(
                sessionToken = tokenProvider.getToken(),
                limit = pageSize,
                offset = offset,
            )
        },
        getNextKey = { page, offset -> offset + page.size },
        onSuccess = onPageLoadSuccess,
        onFailure = onPageLoadFailure,
        endReached = { page, _ -> page.size < pageSize },
    )
    
    suspend fun getClinic(
        clinicId: String,
    ): Result<Clinic> = clinicRepository.getClinic(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
    )
    
    suspend fun findClinicByShareCode(
        shareCode: String,
    ): Result<Clinic> = clinicRepository.getClinicByShareCode(
        sessionToken = tokenProvider.getToken(),
        shareCode = shareCode,
    )
    
    suspend fun createClinic(
        clinic: Clinic,
    ): Result<Clinic> = clinicRepository.createClinic(
        sessionToken = tokenProvider.getToken(),
        clinic = clinic,
    )
    
    suspend fun updateClinic(
        clinicId: String,
        clinic: Clinic,
    ): Result<Clinic> = clinicRepository.updateClinic(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
        clinic = clinic,
    )
    
    suspend fun deleteClinic(
        clinicId: String,
    ): Result<Unit> = clinicRepository.deleteClinic(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
    )
    
    suspend fun getClinicClinicians(
        clinicId: String,
        search: String? = null,
        limit: Int? = 100,
        offset: Int? = 0,
    ): Result<List<Clinician>> = clinicRepository.getClinicClinicians(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
        search = search,
        limit = limit,
        offset = offset,
    )
    
    suspend fun addClinicianToClinic(
        clinicId: String,
        clinician: Clinician,
    ): Result<Clinician> =
        clinicRepository.addClinicianToClinic(
            sessionToken = tokenProvider.getToken(),
            clinicId = clinicId,
            clinician = clinician,
        )
    
    suspend fun getClinicPatients(
        clinicId: String,
        search: String? = null,
        limit: Int? = 100,
        offset: Int? = 0,
        sort: String? = null,
    ): Result<List<Patient>> = clinicRepository.getClinicPatients(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
        search = search,
        limit = limit,
        offset = offset,
        sort = sort,
    )
    
    suspend fun getClinicPatient(
        clinicId: String,
        patientId: String,
    ): Result<Patient> = clinicRepository.getClinicPatient(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
        patientId = patientId,
    )
    
    suspend fun updateClinicPatient(
        clinicId: String,
        patientId: String,
        patient: Patient,
    ): Result<Patient> = clinicRepository.updateClinicPatient(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
        patientId = patientId,
        patient = patient,
    )
    
    suspend fun removePatientFromClinic(
        clinicId: String,
        patientId: String,
    ): Result<Unit> = clinicRepository.removePatientFromClinic(
        sessionToken = tokenProvider.getToken(),
        clinicId = clinicId,
        patientId = patientId,
    )
}