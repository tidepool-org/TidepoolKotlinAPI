package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import org.tidepool.sdk.api.MessageApi
import org.tidepool.sdk.di.provideMessageApi
import org.tidepool.sdk.dto.message.EditMessageDto
import org.tidepool.sdk.dto.message.NewMessageDto
import org.tidepool.sdk.dto.message.toDomain
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.messages.Message
import org.tidepool.sdk.runCatchingNetworkExceptions
import kotlinx.datetime.Instant
import java.time.format.DateTimeFormatter

class MessageRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
) : MessageRepository {

    private val messageApi: MessageApi
        get() = provideMessageApi(environmentRepository.getKtorfit(httpClient))

    override suspend fun listAllMessages(
        sessionToken: String,
        userId: String,
        startTime: Instant?,
        endTime: Instant?,
    ): Result<List<Message>> = runCatchingNetworkExceptions {
        messageApi.listAllMessages(
            sessionToken = sessionToken,
            userId = userId,
            startTime = startTime?.toString(),
            endTime = endTime?.toString(),
        ).messages
    }.mapList { it.toDomain() }
    
    override suspend fun listTopLevelMessages(
        sessionToken: String,
        userId: String,
        startTime: Instant?,
        endTime: Instant?,
    ): Result<List<Message>> = runCatchingNetworkExceptions {
        messageApi.listTopLevelMessages(
            sessionToken = sessionToken,
            userId = userId,
            startTime = startTime?.toString(),
            endTime = endTime?.toString(),
        ).messages
    }.mapList { it.toDomain() }
    
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
                    messageText = messageText,
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
                    messageText = messageText,
                    timestamp = timestamp,
                    guid = guid,
                )
            ),
        ).id
    }
    
    override suspend fun findMessageById(
        sessionToken: String,
        messageId: String,
    ): Result<Message> = runCatchingNetworkExceptions {
        messageApi.findMessageById(
            sessionToken = sessionToken,
            messageId = messageId,
        )
    }.map { it.toDomain() }
    
    override suspend fun getMessageThread(
        sessionToken: String,
        messageId: String,
    ): Result<List<Message>> = runCatchingNetworkExceptions {
        messageApi.getMessageThread(
            sessionToken = sessionToken,
            messageId = messageId,
        ).messages
    }.mapList { it.toDomain() }
    
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
                    messageText = messageText,
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