package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmationLookupDto(
    @SerialName("key")
    val key: String,
)