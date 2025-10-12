package org.tidepool.sdk.dto.clinic

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.Clinician
import java.time.Instant

@Serializable
@KonvertTo(Clinician::class, mapFunctionName = "toDomain")
data class ClinicianDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("inviteId")
    val inviteId: String? = null,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("roles")
    val roles: List<ClinicianRole>,
    @Contextual
    @SerialName("createdTime")
    val createdTime: Instant? = null,
    @Contextual
    @SerialName("updatedTime")
    val updatedTime: Instant? = null
) {
    
    @KonvertFrom(Clinician::class, mapFunctionName = "fromDomain")
    companion object {}
}

@Serializable
enum class ClinicianRole {
    
    CLINIC_ADMIN,
    CLINIC_MEMBER,
    PRESCRIBER
}