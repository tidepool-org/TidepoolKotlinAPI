package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.task.NewTaskDto
import org.tidepool.sdk.dto.task.TaskDto
import org.tidepool.sdk.dto.task.UpdateTaskDto

interface TaskRepository {
    
    suspend fun getTasks(
        sessionToken: String,
        name: String? = null,
        type: String? = null,
        state: String? = null,
        page: Int? = null,
        size: Int? = null
    ): Result<List<TaskDto>>
    
    suspend fun createTask(
        sessionToken: String,
        task: NewTaskDto
    ): Result<TaskDto>
    
    suspend fun getTask(
        sessionToken: String,
        taskId: String
    ): Result<TaskDto>
    
    suspend fun updateTask(
        sessionToken: String,
        taskId: String,
        task: UpdateTaskDto
    ): Result<TaskDto>
    
    suspend fun deleteTask(
        sessionToken: String,
        taskId: String
    ): Result<Unit>
}