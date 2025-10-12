package org.tidepool.sdk.dto.clinic

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.Patient
import org.tidepool.sdk.model.clinic.DataSource
import java.time.Instant

@Serializable
@KonvertTo(Patient::class, mapFunctionName = "toDomain")
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
) {
    @KonvertFrom(Patient::class, mapFunctionName = "fromDomain")
    companion object
}

@Serializable
@KonvertTo(DataSource::class, mapFunctionName = "toDomain")
data class DataSourceDto(
    @SerialName("providerName")
    val providerName: String,
    @SerialName("state")
    val state: String,
    @SerialName("modifiedTime")
    @Contextual val modifiedTime: Instant? = null,
    @SerialName("expirationTime")
    @Contextual val expirationTime: Instant? = null
) {
    @KonvertFrom(DataSource::class, mapFunctionName = "fromDomain")
    companion object
}