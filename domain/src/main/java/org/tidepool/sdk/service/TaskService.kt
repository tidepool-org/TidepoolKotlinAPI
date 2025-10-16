package org.tidepool.sdk.service

import org.tidepool.sdk.Paginator
import org.tidepool.sdk.PaginatorImpl
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.model.task.NewTask
import org.tidepool.sdk.model.task.Task
import org.tidepool.sdk.model.task.TaskState
import org.tidepool.sdk.model.task.UpdateTask
import org.tidepool.sdk.repository.TaskRepository

class TaskService internal constructor(
    private val taskRepository: TaskRepository,
    private val tokenProvider: TokenProvider,
) {
    
    suspend fun getTasksPaginator(
        name: String? = null,
        type: String? = null,
        state: TaskState? = null,
        pageSize: Int = 42,
        onPageLoadSuccess: suspend (tasks: List<Task>, endReached: Boolean) -> Unit,
        onPageLoadFailure: suspend (Throwable) -> Unit,
    ): Paginator<Int, List<Task>> = PaginatorImpl(
        initialKey = 0,
        onRequest = { pageIndex: Int ->
            taskRepository.getTasks(
                sessionToken = tokenProvider.getToken(),
                name = name,
                type = type,
                state = state,
                page = pageIndex,
                size = pageSize,
            )
        },
        getNextKey = { page, offset -> offset + 1 },
        onSuccess = onPageLoadSuccess,
        onFailure = onPageLoadFailure,
        endReached = { page, _ -> page.size < pageSize },
    )
    
    suspend fun createTask(
        task: NewTask
    ): Result<Task> = taskRepository.createTask(
        sessionToken = tokenProvider.getToken(),
        task = task
    )
    
    suspend fun getTask(
        taskId: String
    ): Result<Task> = taskRepository.getTask(
        sessionToken = tokenProvider.getToken(),
        taskId = taskId
    )
    
    suspend fun updateTask(
        taskId: String,
        task: UpdateTask
    ): Result<Task> = taskRepository.updateTask(
        sessionToken = tokenProvider.getToken(),
        taskId = taskId,
        task = task
    )
    
    suspend fun deleteTask(
        taskId: String
    ): Result<Unit> = taskRepository.deleteTask(
        sessionToken = tokenProvider.getToken(),
        taskId = taskId
    )
}