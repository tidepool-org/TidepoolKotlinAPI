package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmationUpsertDto(
    val clinicId: String? = null,
    val invitedBy: String? = null,
)
