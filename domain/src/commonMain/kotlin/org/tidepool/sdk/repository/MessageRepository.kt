package org.tidepool.sdk.repository

import org.tidepool.sdk.model.messages.Message
import kotlinx.datetime.Instant

interface MessageRepository {
    
    suspend fun listAllMessages(
        sessionToken: String,
        userId: String,
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>>
    
    suspend fun listTopLevelMessages(
        sessionToken: String,
        userId: String,
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>>
    
    suspend fun createMessage(
        sessionToken: String,
        userId: String,
        messageText: String,
        timestamp: Instant,
        guid: String,
    ): Result<String>
    
    suspend fun replyToMessage(
        sessionToken: String,
        messageId: String,
        messageText: String,
        timestamp: Instant,
        guid: String,
    ): Result<String>
    
    suspend fun findMessageById(
        sessionToken: String,
        messageId: String,
    ): Result<Message>
    
    suspend fun getMessageThread(
        sessionToken: String,
        messageId: String,
    ): Result<List<Message>>
    
    suspend fun updateMessage(
        sessionToken: String,
        messageId: String,
        messageText: String? = null,
        timestamp: Instant? = null,
    ): Result<Unit>
    
    suspend fun deleteMessage(
        sessionToken: String,
        messageId: String,
    ): Result<Unit>
}