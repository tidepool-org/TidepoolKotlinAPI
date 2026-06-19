package org.tidepool.sdk.api

import org.tidepool.sdk.dto.prescription.ClaimPrescriptionDto
import org.tidepool.sdk.dto.prescription.NewPrescriptionDto
import org.tidepool.sdk.dto.prescription.PrescriptionDto
import org.tidepool.sdk.dto.prescription.UpdatePrescriptionDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface PrescriptionApi {
    
    // Create a new prescription
    @POST("prescriptions")
    suspend fun createPrescription(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Body requestBody: NewPrescriptionDto
    ): PrescriptionDto
    
    // Get prescription by ID
    @GET("prescriptions/{prescriptionId}")
    suspend fun getPrescription(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("prescriptionId") prescriptionId: String
    ): PrescriptionDto
    
    // Update prescription
    @PUT("prescriptions/{prescriptionId}")
    suspend fun updatePrescription(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("prescriptionId") prescriptionId: String,
        @Body requestBody: UpdatePrescriptionDto
    ): PrescriptionDto
    
    // Delete prescription
    @DELETE("prescriptions/{prescriptionId}")
    suspend fun deletePrescription(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("prescriptionId") prescriptionId: String
    )
    
    // Get prescriptions for a patient
    @GET("prescriptions/patient/{patientId}")
    suspend fun getPrescriptionsForPatient(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("patientId") patientId: String,
        @Query("status") status: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): List<PrescriptionDto>
    
    // Get prescriptions by prescriber
    @GET("prescriptions/prescriber/{prescriberId}")
    suspend fun getPrescriptionsByPrescriber(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("prescriberId") prescriberId: String,
        @Query("status") status: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): List<PrescriptionDto>
    
    // Get prescriptions for a clinic
    @GET("prescriptions/clinic/{clinicId}")
    suspend fun getPrescriptionsForClinic(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("clinicId") clinicId: String,
        @Query("status") status: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): List<PrescriptionDto>

    // Claim a prescription by access code and patient birthday
    // Source: iOS TidepoolKit TAPI.swift — POST /v1/patients/{userId}/prescriptions/claim
    @POST("v1/patients/{userId}/prescriptions/claim")
    suspend fun claimPrescription(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body requestBody: ClaimPrescriptionDto
    ): PrescriptionDto
}