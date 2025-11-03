package org.tidepool.sdk.api

import org.tidepool.sdk.dto.prescription.NewPrescriptionDto
import org.tidepool.sdk.dto.prescription.PrescriptionDto
import org.tidepool.sdk.dto.prescription.UpdatePrescriptionDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PrescriptionApi {
    
    // Create a new prescription
    @POST("/prescriptions")
    suspend fun createPrescription(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Body requestBody: NewPrescriptionDto
    ): PrescriptionDto
    
    // Get prescription by ID
    @GET("/prescriptions/{prescriptionId}")
    suspend fun getPrescription(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("prescriptionId") prescriptionId: String
    ): PrescriptionDto
    
    // Update prescription
    @PUT("/prescriptions/{prescriptionId}")
    suspend fun updatePrescription(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("prescriptionId") prescriptionId: String,
        @Body requestBody: UpdatePrescriptionDto
    ): PrescriptionDto
    
    // Delete prescription
    @DELETE("/prescriptions/{prescriptionId}")
    suspend fun deletePrescription(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("prescriptionId") prescriptionId: String
    )
    
    // Get prescriptions for a patient
    @GET("/prescriptions/patient/{patientId}")
    suspend fun getPrescriptionsForPatient(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("patientId") patientId: String,
        @Query("status") status: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): List<PrescriptionDto>
    
    // Get prescriptions by prescriber
    @GET("/prescriptions/prescriber/{prescriberId}")
    suspend fun getPrescriptionsByPrescriber(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("prescriberId") prescriberId: String,
        @Query("status") status: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): List<PrescriptionDto>
    
    // Get prescriptions for a clinic
    @GET("/prescriptions/clinic/{clinicId}")
    suspend fun getPrescriptionsForClinic(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String,
        @Query("status") status: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): List<PrescriptionDto>
}