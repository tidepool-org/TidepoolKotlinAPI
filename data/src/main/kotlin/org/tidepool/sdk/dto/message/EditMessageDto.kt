package org.tidepool.sdk.dto.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class EditMessageDto(
    val message: EditMessageContentDto
) {
    
    @Serializable
    data class EditMessageContentDto(
        val messagetext: String? = null,
        @Contextual val timestamp: Instant? = null,
    )
}