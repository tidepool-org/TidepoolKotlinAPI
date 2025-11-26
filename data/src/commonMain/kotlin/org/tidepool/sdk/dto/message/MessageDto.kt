package org.tidepool.sdk.dto.message

import kotlinx.datetime.Clock
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.messages.Message
import kotlinx.datetime.Instant

@Serializable
data class MessageDto(
    @SerialName("message")
    val message: MessageContentDto = MessageContentDto()
) {
    
    @Serializable
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
        @Contextual val timestamp: Instant = Clock.System.now(),
        @SerialName("createdtime")
        @Contextual val createdTime: Instant = Clock.System.now(),
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

fun MessageDto.toDomain(): Message = Message(
    id = message.id,
    guid = message.guid,
    userId = message.userId,
    groupId = message.groupId,
    timestamp = message.timestamp,
    createdTime = message.createdTime,
    modifiedTime = message.modifiedTime,
    messageText = message.messageText,
)

fun MessageDto.MessageContentDto
    .toDomain(): Message = Message(
    id = id,
    guid = guid,
    userId = userId,
    groupId = groupId,
    timestamp = timestamp,
    createdTime = createdTime,
    modifiedTime = modifiedTime,
    messageText = messageText,
)