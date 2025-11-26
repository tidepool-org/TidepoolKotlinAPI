package org.tidepool.sdk.api

import org.tidepool.sdk.dto.task.NewTaskDto
import org.tidepool.sdk.dto.task.TaskDto
import org.tidepool.sdk.dto.task.UpdateTaskDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface TaskApi {
    
    @GET("v1/tasks")
    suspend fun getTasks(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("state") state: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): List<TaskDto>
    
    @POST("v1/tasks")
    suspend fun createTask(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Body requestBody: NewTaskDto
    ): TaskDto
    
    @GET("v1/tasks/{taskId}")
    suspend fun getTask(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("taskId") taskId: String
    ): TaskDto
    
    @PUT("v1/tasks/{taskId}")
    suspend fun updateTask(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("taskId") taskId: String,
        @Body requestBody: UpdateTaskDto
    ): TaskDto
    
    @DELETE("v1/tasks/{taskId}")
    suspend fun deleteTask(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("taskId") taskId: String
    )
}