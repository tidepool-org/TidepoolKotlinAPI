package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate

@Serializable
data class PatientDto(
    val id: String,
    val email: String? = null,
    val fullName: String? = null,
    val birthDate: String? = null, // ISO date string
    val diagnosisDate: String? = null, // ISO date string
    val targetDevices: List<String>? = null,
    val dataSources: List<DataSourceDto>? = null,
    val patientTags: List<PatientTagDto>? = null,
    @Contextual val createdTime: Instant? = null,
    @Contextual val updatedTime: Instant? = null
)

@Serializable
data class DataSourceDto(
    val providerName: String,
    val state: String,
    @Contextual val modifiedTime: Instant? = null,
    @Contextual val expirationTime: Instant? = null
)