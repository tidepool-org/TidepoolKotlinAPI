package org.tidepool.sdk.dto.task

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.tidepool.sdk.model.task.NewTask
import org.tidepool.sdk.model.task.UpdateTask
import kotlin.collections.component1
import kotlin.collections.component2

@Serializable
data class UpdateTaskDto(
    @SerialName("priority")
    val priority: Int,
    @SerialName("data")
    val data: JsonObject,
    @SerialName("availableTime")
    val availableTime: String,
    @SerialName("expirationTime")
    val expirationTime: String
)

internal fun UpdateTask.toDto() = UpdateTaskDto(
    priority = priority,
    data = data
        .map { (key, value) -> key to Json.encodeToJsonElement(value) }
        .toMap()
        .let { JsonObject(it) },
    availableTime = availableTime.toString(),
    expirationTime = expirationTime.toString(),
)