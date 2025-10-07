package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ClinicianDto(
    val id: String? = null,
    val inviteId: String? = null,
    val email: String,
    val name: String,
    val roles: List<ClinicianRole>,
    @Contextual val createdTime: Instant? = null,
    @Contextual val updatedTime: Instant? = null
)

@Serializable
enum class ClinicianRole {
    
    CLINIC_ADMIN,
    CLINIC_MEMBER,
    PRESCRIBER
}