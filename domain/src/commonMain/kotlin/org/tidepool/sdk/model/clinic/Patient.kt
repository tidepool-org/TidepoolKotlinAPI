package org.tidepool.sdk.model.clinic

import kotlinx.datetime.Instant

data class Patient(
    val id: String,
    val email: String? = null,
    val fullName: String? = null,
    val birthDate: String? = null, // ISO date string
    val diagnosisDate: String? = null, // ISO date string
    val targetDevices: List<String>? = null,
    val dataSources: List<DataSource>? = null,
    val patientTags: List<PatientTag>? = null,
    val createdTime: Instant? = null,
    val updatedTime: Instant? = null
)

data class DataSource(
    val providerName: String,
    val state: String,
    val modifiedTime: Instant? = null,
    val expirationTime: Instant? = null
)