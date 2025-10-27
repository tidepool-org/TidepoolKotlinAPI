package org.tidepool.sdk.service

import org.tidepool.sdk.Paginator
import org.tidepool.sdk.PaginatorImpl
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
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
            tokenProvider.getToken().flatMap {
                clinicRepository.listClinics(
                    sessionToken = it,
                    limit = pageSize,
                    offset = offset,
                )
            }
        },
        getNextKey = { page, offset -> offset + page.size },
        onSuccess = onPageLoadSuccess,
        onFailure = onPageLoadFailure,
        endReached = { page, _ -> page.size < pageSize },
    )
    
    suspend fun getClinic(
        clinicId: String,
    ): Result<Clinic> = tokenProvider.getToken().flatMap {
        clinicRepository.getClinic(
            sessionToken = it,
            clinicId = clinicId,
        )
    }
    
    suspend fun findClinicByShareCode(
        shareCode: String,
    ): Result<Clinic> = tokenProvider.getToken().flatMap {
        clinicRepository.getClinicByShareCode(
            sessionToken = it,
            shareCode = shareCode,
        )
    }
    
    suspend fun createClinic(
        clinic: Clinic,
    ): Result<Clinic> = tokenProvider.getToken().flatMap {
        clinicRepository.createClinic(
            sessionToken = it,
            clinic = clinic,
        )
    }
    
    suspend fun updateClinic(
        clinicId: String,
        clinic: Clinic,
    ): Result<Clinic> = tokenProvider.getToken().flatMap {
        clinicRepository.updateClinic(
            sessionToken = it,
            clinicId = clinicId,
            clinic = clinic,
        )
    }
    
    suspend fun deleteClinic(
        clinicId: String,
    ): Result<Unit> = tokenProvider.getToken().flatMap {
        clinicRepository.deleteClinic(
            sessionToken = it,
            clinicId = clinicId,
        )
    }
    
    suspend fun getClinicClinicians(
        clinicId: String,
        search: String? = null,
        limit: Int? = 100,
        offset: Int? = 0,
    ): Result<List<Clinician>> = tokenProvider.getToken().flatMap {
        clinicRepository.getClinicClinicians(
            sessionToken = it,
            clinicId = clinicId,
            search = search,
            limit = limit,
            offset = offset,
        )
    }
    
    suspend fun addClinicianToClinic(
        clinicId: String,
        clinician: Clinician,
    ): Result<Clinician> =
        tokenProvider.getToken().flatMap {
            clinicRepository.addClinicianToClinic(
                sessionToken = it,
                clinicId = clinicId,
                clinician = clinician,
            )
        }
    
    suspend fun getClinicPatients(
        clinicId: String,
        search: String? = null,
        limit: Int? = 100,
        offset: Int? = 0,
        sort: String? = null,
    ): Result<List<Patient>> = tokenProvider.getToken().flatMap {
        clinicRepository.getClinicPatients(
            sessionToken = it,
            clinicId = clinicId,
            search = search,
            limit = limit,
            offset = offset,
            sort = sort,
        )
    }
    
    suspend fun getClinicPatient(
        clinicId: String,
        patientId: String,
    ): Result<Patient> = tokenProvider.getToken().flatMap {
        clinicRepository.getClinicPatient(
            sessionToken = it,
            clinicId = clinicId,
            patientId = patientId,
        )
    }
    
    suspend fun updateClinicPatient(
        clinicId: String,
        patientId: String,
        patient: Patient,
    ): Result<Patient> = tokenProvider.getToken().flatMap {
        clinicRepository.updateClinicPatient(
            sessionToken = it,
            clinicId = clinicId,
            patientId = patientId,
            patient = patient,
        )
    }
    
    suspend fun removePatientFromClinic(
        clinicId: String,
        patientId: String,
    ): Result<Unit> = tokenProvider.getToken().flatMap {
        clinicRepository.removePatientFromClinic(
            sessionToken = it,
            clinicId = clinicId,
            patientId = patientId,
        )
    }
}