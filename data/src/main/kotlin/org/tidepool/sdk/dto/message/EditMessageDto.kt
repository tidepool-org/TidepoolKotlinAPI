package org.tidepool.sdk.dto.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class EditMessageDto(
    @SerialName("message")
    val message: EditMessageContentDto
) {
    
    @Serializable
    data class EditMessageContentDto(
        @SerialName("messagetext")
        val messagetext: String? = null,
        @SerialName("timestamp")
        @Contextual val timestamp: Instant? = null,
    )
}