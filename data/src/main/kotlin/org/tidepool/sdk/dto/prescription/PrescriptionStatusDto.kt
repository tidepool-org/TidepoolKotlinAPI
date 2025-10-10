package org.tidepool.sdk.dto.prescription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PrescriptionStatusDto {
    
    @SerialName("active")
    Active,
    
    @SerialName("inactive")
    Inactive,
    
    @SerialName("completed")
    Completed,
    
    @SerialName("cancelled")
    Cancelled,
    
    @SerialName("suspended")
    Suspended,
}