package org.tidepool.sdk.dto.task

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

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