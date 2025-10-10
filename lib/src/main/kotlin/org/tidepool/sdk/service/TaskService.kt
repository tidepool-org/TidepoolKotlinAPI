package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.task.NewTask
import org.tidepool.sdk.model.task.Task
import org.tidepool.sdk.model.task.TaskState
import org.tidepool.sdk.model.task.UpdateTask
import org.tidepool.sdk.model.task.toDomain
import org.tidepool.sdk.model.task.toDto
import org.tidepool.sdk.repository.TaskRepository

class TaskService internal constructor(
    private val taskRepository: TaskRepository,
    private val tokenProvider: TokenProvider,
) {
    
    suspend fun getTasks(
        name: String? = null,
        type: String? = null,
        state: TaskState? = null,
        page: Int? = null,
        size: Int? = null
    ): Result<List<Task>> = taskRepository.getTasks(
        sessionToken = tokenProvider.getToken(),
        name = name,
        type = type,
        state = state?.code,
        page = page,
        size = size
    ).mapList { dto -> dto.toDomain() }
    
    suspend fun createTask(
        task: NewTask
    ): Result<Task> = taskRepository.createTask(
        sessionToken = tokenProvider.getToken(),
        task = task.toDto()
    ).map { it.toDomain() }
    
    suspend fun getTask(
        taskId: String
    ): Result<Task> = taskRepository.getTask(
        sessionToken = tokenProvider.getToken(),
        taskId = taskId
    ).map { it.toDomain() }
    
    suspend fun updateTask(
        taskId: String,
        task: UpdateTask
    ): Result<Task> = taskRepository.updateTask(
        sessionToken = tokenProvider.getToken(),
        taskId = taskId,
        task = task.toDto()
    ).map { it.toDomain() }
    
    suspend fun deleteTask(
        taskId: String
    ): Result<Unit> = taskRepository.deleteTask(
        sessionToken = tokenProvider.getToken(),
        taskId = taskId
    )
}