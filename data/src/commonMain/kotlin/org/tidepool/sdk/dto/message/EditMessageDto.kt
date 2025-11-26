package org.tidepool.sdk.dto.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class EditMessageDto(
    @SerialName("message")
    val message: EditMessageContentDto
) {
    
    @Serializable
    data class EditMessageContentDto(
        @SerialName("messagetext")
        val messageText: String? = null,
        @SerialName("timestamp")
        @Contextual val timestamp: Instant? = null,
    )
}