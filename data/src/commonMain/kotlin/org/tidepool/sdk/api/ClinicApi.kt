package org.tidepool.sdk.api

import org.tidepool.sdk.dto.clinic.ClinicDto
import org.tidepool.sdk.dto.clinic.ClinicianDto
import org.tidepool.sdk.dto.clinic.PatientDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface ClinicApi {
    
    @GET("v1/clinics")
    suspend fun listClinics(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("shareCode") shareCode: String? = null,
        @Query("createdTimeStart") createdTimeStart: String? = null,
        @Query("createdTimeEnd") createdTimeEnd: String? = null,
        @Query("ehrEnabled") ehrEnabled: Boolean? = null,
    ): List<ClinicDto>
    
    @POST("v1/clinics")
    suspend fun createClinic(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Body clinic: ClinicDto
    ): ClinicDto
    
    @GET("v1/clinics/share_code/{shareCode}")
    suspend fun getClinicByShareCode(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("shareCode") shareCode: String
    ): ClinicDto
    
    @GET("v1/clinics/{clinicId}")
    suspend fun getClinic(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String
    ): ClinicDto
    
    @PATCH("v1/clinics/{clinicId}")
    suspend fun updateClinic(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String,
        @Body clinic: ClinicDto
    ): ClinicDto
    
    @DELETE("v1/clinics/{clinicId}")
    suspend fun deleteClinic(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String
    ): Unit
    
    // Clinician endpoints
    @GET("v1/clinics/{clinicId}/clinicians")
    suspend fun getClinicClinicians(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String,
        @Query("search") search: String? = null,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("email") email: String? = null,
        @Query("role") role: String? = null
    ): List<ClinicianDto>
    
    @POST("v1/clinics/{clinicId}/clinicians")
    suspend fun addClinicianToClinic(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String,
        @Body clinician: ClinicianDto
    ): ClinicianDto
    
    // Patient endpoints
    @GET("v1/clinics/{clinicId}/patients")
    suspend fun getClinicPatients(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String,
        @Query("search") search: String? = null,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String? = null
    ): List<PatientDto>
    
    @GET("v1/clinics/{clinicId}/patients/{patientId}")
    suspend fun getClinicPatient(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String,
        @Path("patientId") patientId: String
    ): PatientDto
    
    @PATCH("v1/clinics/{clinicId}/patients/{patientId}")
    suspend fun updateClinicPatient(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String,
        @Path("patientId") patientId: String,
        @Body patient: PatientDto
    ): PatientDto
    
    @DELETE("v1/clinics/{clinicId}/patients/{patientId}")
    suspend fun removePatientFromClinic(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String,
        @Path("patientId") patientId: String
    ): Unit
}