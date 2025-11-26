package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.confirmation.ConfirmationLookup

@Serializable
data class ConfirmationLookupDto(
    @SerialName("key")
    val key: String,
)

fun ConfirmationLookupDto.toDomain(): ConfirmationLookup = ConfirmationLookup(
    key = key
)