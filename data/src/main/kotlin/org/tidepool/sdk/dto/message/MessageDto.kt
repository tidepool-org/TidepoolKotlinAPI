package org.tidepool.sdk.dto.message

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.messages.Message
import java.time.Instant

@Serializable
@KonvertTo(Message::class, mapFunctionName = "toDomain")
data class MessageDto(
    @SerialName("message")
    val message: MessageContentDto = MessageContentDto()
) {
    
    @Serializable
    @KonvertTo(Message::class, mapFunctionName = "toDomain")
    data class MessageContentDto(
        @SerialName("id")
        val id: String = "",
        @SerialName("guid")
        val guid: String = "",
        @SerialName("parentmessage")
        val parentMessage: String? = null,
        @SerialName("userid")
        val userId: String = "",
        @SerialName("groupid")
        val groupId: String = "",
        @SerialName("timestamp")
        @Contextual val timestamp: Instant = Instant.now(),
        @SerialName("createdtime")
        @Contextual val createdTime: Instant = Instant.now(),
        @SerialName("modifiedtime")
        @Contextual val modifiedTime: Instant? = null,
        @SerialName("messagetext")
        val messageText: String = "",
        @SerialName("user")
        val user: MessageUserDto? = null,
    )
    
    @Serializable
    data class MessageUserDto(
        @SerialName("fullName")
        val fullName: String = "",
    )
}