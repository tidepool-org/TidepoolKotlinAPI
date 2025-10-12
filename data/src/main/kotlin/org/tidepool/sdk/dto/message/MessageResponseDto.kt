package org.tidepool.sdk.dto.message

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MessageResponseDto(
    @SerialName("id")
    val id: String = ""
)