package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.model.messages.Message
import org.tidepool.sdk.repository.MessageRepository
import org.tidepool.sdk.repository.UserRepository
import kotlinx.datetime.Instant
import java.util.UUID
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MessageService internal constructor(
    private val messageRepository: MessageRepository,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) {

    suspend fun listAllMessages(
        userId: String,
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>> = tokenProvider.getToken().flatMap {
        messageRepository.listAllMessages(
            sessionToken = it,
            userId = userId,
            startTime = startTime,
            endTime = endTime,
        )
    }

    suspend fun listTopLevelMessages(
        userId: String,
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>> = tokenProvider.getToken().flatMap {
        messageRepository.listTopLevelMessages(
            sessionToken = it,
            userId = userId,
            startTime = startTime,
            endTime = endTime,
        )
    }

    suspend fun createMessage(
        userId: String,
        messageText: String,
        timestamp: Instant = Clock.System.now(),
        guid: String = UUID.randomUUID().toString(),
    ): Result<String> = tokenProvider.getToken().flatMap {
        messageRepository.createMessage(
            sessionToken = it,
            userId = userId,
            messageText = messageText,
            timestamp = timestamp,
            guid = guid,
        )
    }

    suspend fun replyToMessage(
        messageId: String,
        messageText: String,
        timestamp: Instant = Clock.System.now(),
        guid: String = UUID.randomUUID().toString(),
    ): Result<String> = tokenProvider.getToken().flatMap {
        messageRepository.replyToMessage(
            sessionToken = it,
            messageId = messageId,
            messageText = messageText,
            timestamp = timestamp,
            guid = guid,
        )
    }

    suspend fun findMessageById(
        messageId: String,
    ): Result<Message> = tokenProvider.getToken().flatMap {
        messageRepository.findMessageById(
            sessionToken = it,
            messageId = messageId,
        )
    }

    suspend fun getMessageThread(
        messageId: String,
    ): Result<List<Message>> = tokenProvider.getToken().flatMap {
        messageRepository.getMessageThread(
            sessionToken = it,
            messageId = messageId,
        )
    }

    suspend fun updateMessage(
        messageId: String,
        messageText: String? = null,
        timestamp: Instant? = null,
    ): Result<Unit> = tokenProvider.getToken().flatMap {
        messageRepository.updateMessage(
            sessionToken = it,
            messageId = messageId,
            messageText = messageText,
            timestamp = timestamp,
        )
    }

    suspend fun deleteMessage(
        messageId: String,
    ): Result<Unit> = tokenProvider.getToken().flatMap {
        messageRepository.deleteMessage(
            sessionToken = it,
            messageId = messageId,
        )
    }

    suspend fun getCurrentUserMessages(
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>> = tokenProvider.getToken().flatMap { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            messageRepository.listAllMessages(
                sessionToken = token,
                userId = user.userId,
                startTime = startTime,
                endTime = endTime,
            )
        }
    }

    suspend fun getCurrentUserTopLevelMessages(
        startTime: Instant? = null,
        endTime: Instant? = null,
    ): Result<List<Message>> = tokenProvider.getToken().flatMap { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            messageRepository.listTopLevelMessages(
                sessionToken = token,
                userId = user.userId,
                startTime = startTime,
                endTime = endTime,
            )
        }
    }

    suspend fun createMessageForCurrentUser(
        messageText: String,
        timestamp: Instant = Clock.System.now(),
        guid: String = UUID.randomUUID().toString(),
    ): Result<String> = tokenProvider.getToken().flatMap { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            messageRepository.createMessage(
                sessionToken = token,
                userId = user.userId,
                messageText = messageText,
                timestamp = timestamp,
                guid = guid,
            )
        }
    }
}