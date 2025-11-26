package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.Clinician
import org.tidepool.sdk.model.clinic.ClinicianRole
import kotlinx.datetime.Instant

@Serializable
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
    val roles: List<ClinicianRoleDto>,
    @Contextual
    @SerialName("createdTime")
    val createdTime: Instant? = null,
    @Contextual
    @SerialName("updatedTime")
    val updatedTime: Instant? = null,
)

@Serializable
enum class ClinicianRoleDto {
    @SerialName("CLINIC_ADMIN")
    ClinicAdmin,

    @SerialName("CLINIC_MEMBER")
    ClinicMember,

    @SerialName("PRESCRIBER")
    Prescriber,
    ;
}

fun ClinicianDto.toDomain(): Clinician = Clinician(
    id = id,
    inviteId = inviteId,
    email = email,
    name = name,
    roles = roles.map { it.toDomain() },
    createdTime = createdTime,
    updatedTime = updatedTime
)

private fun ClinicianRoleDto.toDomain(): ClinicianRole = when (this) {
    ClinicianRoleDto.ClinicAdmin -> ClinicianRole.ClinicAdmin
    ClinicianRoleDto.ClinicMember -> ClinicianRole.ClinicMember
    ClinicianRoleDto.Prescriber -> ClinicianRole.Prescriber
}

fun Clinician.toDto(): ClinicianDto = ClinicianDto(
    id = id,
    inviteId = inviteId,
    email = email,
    name = name,
    roles = roles.map {
        it.toDto()
    },
    createdTime = createdTime,
    updatedTime = updatedTime
)

private fun ClinicianRole.toDto(): ClinicianRoleDto = when (this) {
    ClinicianRole.ClinicAdmin -> ClinicianRoleDto.ClinicAdmin
    ClinicianRole.ClinicMember -> ClinicianRoleDto.ClinicMember
    ClinicianRole.Prescriber -> ClinicianRoleDto.Prescriber
}