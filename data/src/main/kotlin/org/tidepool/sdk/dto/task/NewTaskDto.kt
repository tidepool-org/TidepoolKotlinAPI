package org.tidepool.sdk.dto.task

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.tidepool.sdk.model.task.NewTask

@Serializable
data class NewTaskDto(
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
    val expirationTime: String
)

internal fun NewTask.toDto() = NewTaskDto(
    name = name,
    type = type,
    priority = priority,
    data = data
        .map { (key, value) -> key to Json.encodeToJsonElement(value) }
        .toMap()
        .let { JsonObject(it) },
    availableTime = availableTime.toString(),
    expirationTime = expirationTime.toString(),
)