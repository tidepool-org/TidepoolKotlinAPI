package org.tidepool.sdk.dto.task

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.tidepool.sdk.model.task.Task
import org.tidepool.sdk.model.task.TaskState
import java.time.Instant

@Serializable
data class TaskDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("type")
    val type: String,
    @SerialName("priority")
    val priority: Int,
    @SerialName("data")
    val data: JsonObject,
    @SerialName("availableTime")
    val availableTime: String,
    @SerialName("expirationTime")
    val expirationTime: String,
    @SerialName("state")
    val state: TaskStateDto,
    @SerialName("error")
    val error: String? = null,
    @SerialName("runTime")
    val runTime: String? = null,
    @SerialName("duration")
    val duration: Double? = null,
    @SerialName("createdTime")
    val createdTime: String? = null,
    @SerialName("modifiedTime")
    val modifiedTime: String? = null,
)

// Manual mapping functions
internal fun TaskDto.toDomain(): Task = Task(
    id = id,
    name = name,
    type = type,
    priority = priority,
    data = data.entries.associate { (key, value) ->
        key to when (value) {
            is JsonPrimitive -> when {
                value.isString -> value.content
                else           -> value.content
            }
            
            else             -> value.toString()
        }
    },
    availableTime = Instant.parse(availableTime),
    expirationTime = Instant.parse(expirationTime),
    state = state.toDomain(),
    error = error,
    runTime = runTime?.let { Instant.parse(it) },
    duration = duration,
    createdTime = createdTime?.let { Instant.parse(it) },
    modifiedTime = modifiedTime?.let { Instant.parse(it) }
)

internal fun TaskStateDto.toDomain(): TaskState = when (this) {
    TaskStateDto.Pending -> TaskState.Pending
    TaskStateDto.Running -> TaskState.Running
    TaskStateDto.Failed -> TaskState.Failed
    TaskStateDto.Completed -> TaskState.Completed
}