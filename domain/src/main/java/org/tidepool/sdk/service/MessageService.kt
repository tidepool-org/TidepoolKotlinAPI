package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.messages.Message
import org.tidepool.sdk.repository.MessageRepository
import org.tidepool.sdk.repository.UserRepository
import java.time.Instant
import java.util.UUID

class MessageService internal constructor(
    private val messageRepository: MessageRepository,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) {
    
    suspend fun listAllMessages(
        userId: String,
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>> = messageRepository.listAllMessages(
        sessionToken = tokenProvider.getToken(),
        userId = userId,
        startTime = startTime,
        endTime = endTime
    )
    
    suspend fun listTopLevelMessages(
        userId: String,
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>> = messageRepository.listTopLevelMessages(
        sessionToken = tokenProvider.getToken(),
        userId = userId,
        startTime = startTime,
        endTime = endTime
    )
    
    suspend fun createMessage(
        userId: String,
        messageText: String,
        timestamp: Instant = Instant.now(),
        guid: String = UUID.randomUUID().toString(),
    ): Result<String> = messageRepository.createMessage(
        sessionToken = tokenProvider.getToken(),
        userId = userId,
        messageText = messageText,
        timestamp = timestamp,
        guid = guid
    )
    
    suspend fun replyToMessage(
        messageId: String,
        messageText: String,
        timestamp: Instant = Instant.now(),
        guid: String = UUID.randomUUID().toString(),
    ): Result<String> = messageRepository.replyToMessage(
        sessionToken = tokenProvider.getToken(),
        messageId = messageId,
        messageText = messageText,
        timestamp = timestamp,
        guid = guid
    )
    
    suspend fun findMessageById(
        messageId: String,
    ): Result<Message> = messageRepository.findMessageById(
        sessionToken = tokenProvider.getToken(),
        messageId = messageId
    )
    
    suspend fun getMessageThread(
        messageId: String,
    ): Result<List<Message>> = messageRepository.getMessageThread(
        sessionToken = tokenProvider.getToken(),
        messageId = messageId
    )
    
    suspend fun updateMessage(
        messageId: String,
        messageText: String? = null,
        timestamp: Instant? = null,
    ): Result<Unit> = messageRepository.updateMessage(
        sessionToken = tokenProvider.getToken(),
        messageId = messageId,
        messageText = messageText,
        timestamp = timestamp
    )
    
    suspend fun deleteMessage(
        messageId: String,
    ): Result<Unit> = messageRepository.deleteMessage(
        sessionToken = tokenProvider.getToken(),
        messageId = messageId
    )
    
    suspend fun getCurrentUserMessages(
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            messageRepository.listAllMessages(
                sessionToken = token,
                userId = user.userId,
                startTime = startTime,
                endTime = endTime
            )
        }
    }
    
    suspend fun getCurrentUserTopLevelMessages(
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            messageRepository.listTopLevelMessages(
                sessionToken = token,
                userId = user.userId,
                startTime = startTime,
                endTime = endTime
            )
        }
    }
    
    suspend fun createMessageForCurrentUser(
        messageText: String,
        timestamp: Instant = Instant.now(),
        guid: String = UUID.randomUUID().toString(),
    ): Result<String> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            messageRepository.createMessage(
                sessionToken = token,
                userId = user.userId,
                messageText = messageText,
                timestamp = timestamp,
                guid = guid
            )
        }
    }
}