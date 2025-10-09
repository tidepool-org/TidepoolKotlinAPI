package org.tidepool.sdk.model.messages

import org.tidepool.sdk.dto.message.MessageDto
import java.time.Instant

data class Message(
    val id: String = "",
    val guid: String = "",
    val parentMessageId: String? = null,
    val userId: String = "",
    val groupId: String = "",
    val timestamp: Instant = Instant.now(),
    val createdTime: Instant = Instant.now(),
    val modifiedTime: Instant? = null,
    val messageText: String = "",
    val userName: String? = null,
)

internal fun MessageDto.toDomain(): Message = Message(
    id = message.id,
    guid = message.guid,
    parentMessageId = message.parentmessage,
    userId = message.userid,
    groupId = message.groupid,
    timestamp = message.timestamp,
    createdTime = message.createdtime,
    modifiedTime = message.modifiedtime,
    messageText = message.messagetext,
    userName = message.user?.fullName,
)