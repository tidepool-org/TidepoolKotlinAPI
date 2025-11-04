package org.tidepool.sdk.service

import org.tidepool.sdk.Paginator
import org.tidepool.sdk.PaginatorImpl
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
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
            tokenProvider.getToken().flatMap {
                taskRepository.getTasks(
                    sessionToken = it,
                    name = name,
                    type = type,
                    state = state,
                    page = pageIndex,
                    size = pageSize,
                )
            }
        },
        getNextKey = { page, offset -> offset + 1 },
        onSuccess = onPageLoadSuccess,
        onFailure = onPageLoadFailure,
        endReached = { page, _ -> page.size < pageSize },
    )
    
    suspend fun createTask(
        task: NewTask
    ): Result<Task> = tokenProvider.getToken().flatMap {
        taskRepository.createTask(
            sessionToken = it,
            task = task
        )
    }
    
    suspend fun getTask(
        taskId: String
    ): Result<Task> = tokenProvider.getToken().flatMap {
        taskRepository.getTask(
            sessionToken = it,
            taskId = taskId
        )
    }
    
    suspend fun updateTask(
        taskId: String,
        task: UpdateTask
    ): Result<Task> = tokenProvider.getToken().flatMap {
        taskRepository.updateTask(
            sessionToken = it,
            taskId = taskId,
            task = task
        )
    }
    
    suspend fun deleteTask(
        taskId: String
    ): Result<Unit> = tokenProvider.getToken().flatMap {
        taskRepository.deleteTask(
            sessionToken = it,
            taskId = taskId
        )
    }
}