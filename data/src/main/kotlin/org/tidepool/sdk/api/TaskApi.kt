package org.tidepool.sdk.api

import org.tidepool.sdk.dto.task.NewTaskDto
import org.tidepool.sdk.dto.task.TaskDto
import org.tidepool.sdk.dto.task.UpdateTaskDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {
    
    @GET("/v1/tasks")
    suspend fun getTasks(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("state") state: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): List<TaskDto>
    
    @POST("/v1/tasks")
    suspend fun createTask(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Body requestBody: NewTaskDto
    ): TaskDto
    
    @GET("/v1/tasks/{taskId}")
    suspend fun getTask(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("taskId") taskId: String
    ): TaskDto
    
    @PUT("/v1/tasks/{taskId}")
    suspend fun updateTask(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("taskId") taskId: String,
        @Body requestBody: UpdateTaskDto
    ): TaskDto
    
    @DELETE("/v1/tasks/{taskId}")
    suspend fun deleteTask(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("taskId") taskId: String
    )
}