package org.tidepool.sdk.dto.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class NewMessageDto(
    @SerialName("message")
    val message: NewMessageContentDto
) {
    
    @Serializable
    data class NewMessageContentDto(
        @SerialName("messagetext")
        val messagetext: String,
        @SerialName("timestamp")
        @Contextual val timestamp: Instant,
        @SerialName("guid")
        val guid: String,
    )
}