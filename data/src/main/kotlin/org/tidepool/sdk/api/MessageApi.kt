package org.tidepool.sdk.api

import org.tidepool.sdk.dto.message.EditMessageDto
import org.tidepool.sdk.dto.message.MessageDto
import org.tidepool.sdk.dto.message.MessageListDto
import org.tidepool.sdk.dto.message.MessageResponseDto
import org.tidepool.sdk.dto.message.NewMessageDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageApi {
    
    @GET("/message/all/{userId}")
    suspend fun listAllMessages(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("starttime") startTime: String? = null,
        @Query("endtime") endTime: String? = null,
    ): MessageListDto
    
    @GET("/message/notes/{userId}")
    suspend fun listTopLevelMessages(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("starttime") startTime: String? = null,
        @Query("endtime") endTime: String? = null,
    ): MessageListDto
    
    @POST("/message/send/{userId}")
    suspend fun createMessage(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body requestBody: NewMessageDto,
    ): MessageResponseDto
    
    @POST("/message/reply/{messageId}")
    suspend fun replyToMessage(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
        @Body requestBody: NewMessageDto,
    ): MessageResponseDto
    
    @GET("/message/read/{messageId}")
    suspend fun findMessageById(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
    ): MessageDto
    
    @GET("/message/thread/{messageId}")
    suspend fun getMessageThread(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
    ): MessageListDto
    
    @PUT("/message/edit/{messageId}")
    suspend fun updateMessage(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
        @Body requestBody: EditMessageDto,
    )
    
    @DELETE("/message/remove/{messageId}")
    suspend fun deleteMessage(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("messageId") messageId: String,
    )
}