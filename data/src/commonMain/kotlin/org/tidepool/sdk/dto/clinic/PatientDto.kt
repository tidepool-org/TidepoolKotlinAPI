package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.DataSource
import org.tidepool.sdk.model.clinic.Patient
import java.time.Instant

@Serializable
data class PatientDto(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("fullName")
    val fullName: String? = null,
    @SerialName("birthDate")
    val birthDate: String? = null, // ISO date string
    @SerialName("diagnosisDate")
    val diagnosisDate: String? = null, // ISO date string
    @SerialName("targetDevices")
    val targetDevices: List<String>? = null,
    @SerialName("dataSources")
    val dataSources: List<DataSourceDto>? = null,
    @SerialName("patientTags")
    val patientTags: List<PatientTagDto>? = null,
    @SerialName("createdTime")
    @Contextual val createdTime: Instant? = null,
    @SerialName("updatedTime")
    @Contextual val updatedTime: Instant? = null
)

fun PatientDto.toDomain(): Patient = Patient(
    id = id,
    email = email,
    fullName = fullName,
    birthDate = birthDate,
    diagnosisDate = diagnosisDate,
    targetDevices = targetDevices,
    dataSources = dataSources?.map { it.toDomain() },
    patientTags = patientTags?.map { it.toDomain() },
    createdTime = createdTime,
    updatedTime = updatedTime
)

fun Patient.toDto(): PatientDto = PatientDto(
    id = id,
    email = email,
    fullName = fullName,
    birthDate = birthDate,
    diagnosisDate = diagnosisDate,
    targetDevices = targetDevices,
    dataSources = dataSources?.map { it.toDto() },
    patientTags = patientTags?.map { it.toDto() },
    createdTime = createdTime,
    updatedTime = updatedTime
)

@Serializable
data class DataSourceDto(
    @SerialName("providerName")
    val providerName: String,
    @SerialName("state")
    val state: String,
    @SerialName("modifiedTime")
    @Contextual val modifiedTime: Instant? = null,
    @SerialName("expirationTime")
    @Contextual val expirationTime: Instant? = null
)

fun DataSourceDto.toDomain(): DataSource = DataSource(
    providerName = providerName,
    state = state,
    modifiedTime = modifiedTime,
    expirationTime = expirationTime,
)

fun DataSource.toDto(): DataSourceDto = DataSourceDto(
    providerName = providerName,
    state = state,
    modifiedTime = modifiedTime,
    expirationTime = expirationTime,
)