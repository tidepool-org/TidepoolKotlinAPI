package org.tidepool.sdk.dto.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageListDto(
    val messages: List<MessageDto> = emptyList()
)