package org.tidepool.sdk.dto.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class NewMessageDto(
    val message: NewMessageContentDto
) {
    
    @Serializable
    data class NewMessageContentDto(
        val messagetext: String,
        @Contextual val timestamp: Instant,
        val guid: String,
    )
}