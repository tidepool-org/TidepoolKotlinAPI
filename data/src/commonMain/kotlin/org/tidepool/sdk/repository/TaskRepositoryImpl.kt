package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import org.tidepool.sdk.api.TaskApi
import org.tidepool.sdk.dto.task.toDomain
import org.tidepool.sdk.dto.task.toDto
import org.tidepool.sdk.di.provideTaskApi
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.task.NewTask
import org.tidepool.sdk.model.task.Task
import org.tidepool.sdk.model.task.TaskState
import org.tidepool.sdk.model.task.UpdateTask
import org.tidepool.sdk.runCatchingNetworkExceptions

class TaskRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
) : TaskRepository {

    private val taskApi: TaskApi
        get() = provideTaskApi(environmentRepository.getKtorfit(httpClient))

    override suspend fun getTasks(
        sessionToken: String,
        name: String?,
        type: String?,
        state: TaskState?,
        page: Int?,
        size: Int?,
    ): Result<List<Task>> {
        return runCatchingNetworkExceptions {
            taskApi.getTasks(
                sessionToken = sessionToken,
                name = name,
                type = type,
                state = state?.name,
                page = page,
                size = size
            )
        }.mapList { it.toDomain() }
    }
    
    override suspend fun createTask(
        sessionToken: String,
        task: NewTask
    ): Result<Task> = runCatchingNetworkExceptions {
        taskApi.createTask(
            sessionToken = sessionToken,
            requestBody = task.toDto(),
        )
    }.map { it.toDomain() }
    
    override suspend fun getTask(
        sessionToken: String,
        taskId: String
    ): Result<Task> = runCatchingNetworkExceptions {
        taskApi.getTask(
            sessionToken = sessionToken,
            taskId = taskId
        )
    }.map { it.toDomain() }
    
    override suspend fun updateTask(
        sessionToken: String,
        taskId: String,
        task: UpdateTask
    ): Result<Task> = runCatchingNetworkExceptions {
        taskApi.updateTask(
            sessionToken = sessionToken,
            taskId = taskId,
            requestBody = task.toDto(),
        )
    }.map { it.toDomain() }
    
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