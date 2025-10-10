package org.tidepool.sdk.model.task

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.tidepool.sdk.dto.task.NewTaskDto
import org.tidepool.sdk.dto.task.TaskDto
import org.tidepool.sdk.dto.task.TaskStateDto
import org.tidepool.sdk.dto.task.UpdateTaskDto
import java.time.Instant

data class Task(
    val id: String,
    val name: String,
    val type: String,
    val priority: Int,
    val data: Map<String, Any>,
    val availableTime: Instant,
    val expirationTime: Instant,
    val state: TaskState? = null,
    val error: String? = null,
    val runTime: Instant? = null,
    val duration: Double? = null,
    val createdTime: Instant? = null,
    val modifiedTime: Instant? = null
)

data class NewTask(
    val name: String,
    val type: String,
    val priority: Int,
    val data: Map<String, Any>,
    val availableTime: Instant,
    val expirationTime: Instant,
)

data class UpdateTask(
    val priority: Int,
    val data: Map<String, Any>,
    val availableTime: Instant,
    val expirationTime: Instant,
)

internal fun TaskDto.toDomain(): Task = Task(
    id = id,
    name = name,
    type = type,
    priority = priority,
    data = data,
    availableTime = Instant.parse(availableTime),
    expirationTime = Instant.parse(expirationTime),
    state = state.toDomain(),
    error = error,
    runTime = runTime?.let { Instant.parse(it) },
    duration = duration,
    createdTime = createdTime?.let { Instant.parse(it) },
    modifiedTime = modifiedTime?.let { Instant.parse(it) },
)

internal fun NewTask.toDto(): NewTaskDto = NewTaskDto(
    name = name,
    type = type,
    priority = priority,
    data = JsonObject(
        content = data.mapValues { Json.encodeToJsonElement(it.value) }
    ),
    availableTime = availableTime.toString(),
    expirationTime = expirationTime.toString(),
)

internal fun UpdateTask.toDto(): UpdateTaskDto = UpdateTaskDto(
    priority = priority,
    data = JsonObject(
        content = data.mapValues { Json.encodeToJsonElement(it.value) }
    ),
    availableTime = availableTime.toString(),
    expirationTime = expirationTime.toString(),
)

internal fun TaskStateDto.toDomain(): TaskState? = when (this) {
    TaskStateDto.Pending -> TaskState.Pending
    TaskStateDto.Running -> TaskState.Running
    TaskStateDto.Failed -> TaskState.Failed
    TaskStateDto.Completed -> TaskState.Completed
}