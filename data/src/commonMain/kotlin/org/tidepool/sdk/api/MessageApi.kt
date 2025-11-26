package org.tidepool.sdk.api

import org.tidepool.sdk.dto.message.EditMessageDto
import org.tidepool.sdk.dto.message.MessageDto
import org.tidepool.sdk.dto.message.MessageListDto
import org.tidepool.sdk.dto.message.MessageResponseDto
import org.tidepool.sdk.dto.message.NewMessageDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface MessageApi {
    
    @GET("message/all/{userId}")
    suspend fun listAllMessages(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("starttime") startTime: String? = null,
        @Query("endtime") endTime: String? = null,
    ): MessageListDto
    
    @GET("message/notes/{userId}")
    suspend fun listTopLevelMessages(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("starttime") startTime: String? = null,
        @Query("endtime") endTime: String? = null,
    ): MessageListDto
    
    @POST("message/send/{userId}")
    suspend fun createMessage(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body requestBody: NewMessageDto,
    ): MessageResponseDto
    
    @POST("message/reply/{messageId}")
    suspend fun replyToMessage(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
        @Body requestBody: NewMessageDto,
    ): MessageResponseDto
    
    @GET("message/read/{messageId}")
    suspend fun findMessageById(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
    ): MessageDto
    
    @GET("message/thread/{messageId}")
    suspend fun getMessageThread(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
    ): MessageListDto
    
    @PUT("message/edit/{messageId}")
    suspend fun updateMessage(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
        @Body requestBody: EditMessageDto,
    )
    
    @DELETE("message/remove/{messageId}")
    suspend fun deleteMessage(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
    )
}