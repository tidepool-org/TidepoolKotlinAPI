package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.TaskApi
import org.tidepool.sdk.dto.task.NewTaskDto
import org.tidepool.sdk.dto.task.TaskDto
import org.tidepool.sdk.dto.task.UpdateTaskDto
import org.tidepool.sdk.repository.TaskRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

class TaskRepositoryImpl(
    private val taskApi: TaskApi,
) : TaskRepository {
    
    override suspend fun getTasks(
        sessionToken: String,
        name: String?,
        type: String?,
        state: String?,
        page: Int?,
        size: Int?
    ): Result<List<TaskDto>> = runCatchingNetworkExceptions {
        taskApi.getTasks(
            sessionToken = sessionToken,
            name = name,
            type = type,
            state = state,
            page = page,
            size = size
        )
    }
    
    override suspend fun createTask(
        sessionToken: String,
        task: NewTaskDto
    ): Result<TaskDto> = runCatchingNetworkExceptions {
        taskApi.createTask(
            sessionToken = sessionToken,
            requestBody = task
        )
    }
    
    override suspend fun getTask(
        sessionToken: String,
        taskId: String
    ): Result<TaskDto> = runCatchingNetworkExceptions {
        taskApi.getTask(
            sessionToken = sessionToken,
            taskId = taskId
        )
    }
    
    override suspend fun updateTask(
        sessionToken: String,
        taskId: String,
        task: UpdateTaskDto
    ): Result<TaskDto> = runCatchingNetworkExceptions {
        taskApi.updateTask(
            sessionToken = sessionToken,
            taskId = taskId,
            requestBody = task
        )
    }
    
    override suspend fun deleteTask(
        sessionToken: String,
        taskId: String
    ): Result<Unit> = runCatchingNetworkExceptions {
        taskApi.deleteTask(
            sessionToken = sessionToken,
            taskId = taskId
        )
    }
}