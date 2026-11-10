package org.tidepool.sdk.model.messages

import kotlin.time.Clock
import kotlin.time.Instant

data class Message(
    val id: String = "",
    val guid: String = "",
    val parentMessageId: String? = null,
    val userId: String = "",
    val groupId: String = "",
    val timestamp: Instant = Clock.System.now(),
    val createdTime: Instant = Clock.System.now(),
    val modifiedTime: Instant? = null,
    val messageText: String = "",
    val userName: String? = null,
)