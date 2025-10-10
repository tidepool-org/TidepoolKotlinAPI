package org.tidepool.sdk.dto.task

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

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