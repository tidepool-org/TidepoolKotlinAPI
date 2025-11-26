package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmationUpsertDto(
    @SerialName("clinicId")
    val clinicId: String? = null,
    @SerialName("invitedBy")
    val invitedBy: String? = null,
)
