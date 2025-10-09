package org.tidepool.sdk.dto.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseDto(
    val id: String = ""
)