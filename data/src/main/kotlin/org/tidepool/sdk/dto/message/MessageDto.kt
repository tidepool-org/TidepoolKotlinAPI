package org.tidepool.sdk.dto.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class MessageDto(
    val message: MessageContentDto = MessageContentDto()
) {
    
    @Serializable
    data class MessageContentDto(
        val id: String = "",
        val guid: String = "",
        val parentmessage: String? = null,
        val userid: String = "",
        val groupid: String = "",
        @Contextual val timestamp: Instant = Instant.now(),
        @Contextual val createdtime: Instant = Instant.now(),
        @Contextual val modifiedtime: Instant? = null,
        val messagetext: String = "",
        val user: MessageUserDto? = null,
    )
    
    @Serializable
    data class MessageUserDto(
        val fullName: String = "",
    )
}