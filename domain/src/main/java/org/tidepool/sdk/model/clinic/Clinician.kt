package org.tidepool.sdk.model.clinic

import java.time.Instant

data class Clinician(
    val id: String? = null,
    val inviteId: String? = null,
    val email: String,
    val name: String,
    val roles: List<ClinicianRole>,
    val createdTime: Instant? = null,
    val updatedTime: Instant? = null
)

enum class ClinicianRole {
    ClinicAdmin,
    ClinicMember,
    Prescriber,
    ;
}