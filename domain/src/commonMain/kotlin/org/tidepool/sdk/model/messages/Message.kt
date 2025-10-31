package org.tidepool.sdk.model.messages

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