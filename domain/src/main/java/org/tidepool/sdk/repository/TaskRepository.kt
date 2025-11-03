package org.tidepool.sdk.repository

import org.tidepool.sdk.model.task.NewTask
import org.tidepool.sdk.model.task.Task
import org.tidepool.sdk.model.task.TaskState
import org.tidepool.sdk.model.task.UpdateTask

interface TaskRepository {
    
    suspend fun getTasks(
        sessionToken: String,
        name: String? = null,
        type: String? = null,
        state: TaskState? = null,
        page: Int? = null,
        size: Int? = null
    ): Result<List<Task>>
    
    suspend fun createTask(
        sessionToken: String,
        task: NewTask
    ): Result<Task>
    
    suspend fun getTask(
        sessionToken: String,
        taskId: String
    ): Result<Task>
    
    suspend fun updateTask(
        sessionToken: String,
        taskId: String,
        task: UpdateTask
    ): Result<Task>
    
    suspend fun deleteTask(
        sessionToken: String,
        taskId: String
    ): Result<Unit>
}