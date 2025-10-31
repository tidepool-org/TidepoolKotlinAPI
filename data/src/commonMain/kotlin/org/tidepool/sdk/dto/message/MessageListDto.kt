package org.tidepool.sdk.dto.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageListDto(
    @SerialName("messages")
    val messages: List<MessageDto.MessageContentDto> = emptyList()
)