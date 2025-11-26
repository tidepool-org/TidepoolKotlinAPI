package org.tidepool.sdk.model.task

import kotlinx.datetime.Instant

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