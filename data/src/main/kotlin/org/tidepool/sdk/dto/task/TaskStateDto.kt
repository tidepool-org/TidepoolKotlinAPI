package org.tidepool.sdk.dto.task

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TaskStateDto {
    
    @SerialName("pending")
    Pending,
    
    @SerialName("running")
    Running,
    
    @SerialName("failed")
    Failed,
    
    @SerialName("completed")
    Completed,
    ;
}