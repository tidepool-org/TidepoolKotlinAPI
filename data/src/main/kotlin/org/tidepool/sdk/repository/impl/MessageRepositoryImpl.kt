package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.MessageApi
import org.tidepool.sdk.dto.message.EditMessageDto
import org.tidepool.sdk.dto.message.MessageDto
import org.tidepool.sdk.dto.message.NewMessageDto
import org.tidepool.sdk.repository.MessageRepository
import org.tidepool.sdk.runCatchingNetworkExceptions
import java.time.Instant
import java.time.format.DateTimeFormatter

class MessageRepositoryImpl(
    private val messageApi: MessageApi,
) : MessageRepository {
    
    override suspend fun listAllMessages(
        sessionToken: String,
        userId: String,
        startTime: Instant?,
        endTime: Instant?,
    ): Result<List<MessageDto>> = runCatchingNetworkExceptions {
        messageApi.listAllMessages(
            sessionToken = sessionToken,
            userId = userId,
            startTime = startTime?.let { DateTimeFormatter.ISO_INSTANT.format(it) },
            endTime = endTime?.let { DateTimeFormatter.ISO_INSTANT.format(it) },
        ).messages
    }
    
    override suspend fun listTopLevelMessages(
        sessionToken: String,
        userId: String,
        startTime: Instant?,
        endTime: Instant?,
    ): Result<List<MessageDto>> = runCatchingNetworkExceptions {
        messageApi.listTopLevelMessages(
            sessionToken = sessionToken,
            userId = userId,
            startTime = startTime?.let { DateTimeFormatter.ISO_INSTANT.format(it) },
            endTime = endTime?.let { DateTimeFormatter.ISO_INSTANT.format(it) },
        ).messages
    }
    
    override suspend fun createMessage(
        sessionToken: String,
        userId: String,
        messageText: String,
        timestamp: Instant,
        guid: String,
    ): Result<String> = runCatchingNetworkExceptions {
        messageApi.createMessage(
            sessionToken = sessionToken,
            userId = userId,
            requestBody = NewMessageDto(
                message = NewMessageDto.NewMessageContentDto(
                    messagetext = messageText,
                    timestamp = timestamp,
                    guid = guid,
                )
            ),
        ).id
    }
    
    override suspend fun replyToMessage(
        sessionToken: String,
        messageId: String,
        messageText: String,
        timestamp: Instant,
        guid: String,
    ): Result<String> = runCatchingNetworkExceptions {
        messageApi.replyToMessage(
            sessionToken = sessionToken,
            messageId = messageId,
            requestBody = NewMessageDto(
                message = NewMessageDto.NewMessageContentDto(
                    messagetext = messageText,
                    timestamp = timestamp,
                    guid = guid,
                )
            ),
        ).id
    }
    
    override suspend fun findMessageById(
        sessionToken: String,
        messageId: String,
    ): Result<MessageDto> = runCatchingNetworkExceptions {
        messageApi.findMessageById(
            sessionToken = sessionToken,
            messageId = messageId,
        )
    }
    
    override suspend fun getMessageThread(
        sessionToken: String,
        messageId: String,
    ): Result<List<MessageDto>> = runCatchingNetworkExceptions {
        messageApi.getMessageThread(
            sessionToken = sessionToken,
            messageId = messageId,
        ).messages
    }
    
    override suspend fun updateMessage(
        sessionToken: String,
        messageId: String,
        messageText: String?,
        timestamp: Instant?,
    ): Result<Unit> = runCatchingNetworkExceptions {
        messageApi.updateMessage(
            sessionToken = sessionToken,
            messageId = messageId,
            requestBody = EditMessageDto(
                message = EditMessageDto.EditMessageContentDto(
                    messagetext = messageText,
                    timestamp = timestamp,
                )
            ),
        )
    }
    
    override suspend fun deleteMessage(
        sessionToken: String,
        messageId: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        messageApi.deleteMessage(
            sessionToken = sessionToken,
            messageId = messageId,
        )
    }
}