package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: Finish implementing automated.v1
@Serializable
data class BasalAutomatedDataDto(
    val deliveryType: DeliveryTypeDto,
    val duration: Int,
    val expectedDuration: Int? = null,
    val rate: Double = -1.0,
    val scheduleName: String? = null,
) : BaseDataDto(DataTypeDto.Basal) {
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
    val suppressed: Nothing
        get() = TODO("schema \"scheduled.v1\" not implemented")
    
    @Serializable
    enum class DeliveryTypeDto {
        
        @SerialName("automated")
        Automated,
        
        @SerialName("scheduled")
        Scheduled,
        
        @SerialName("suspend")
        Suspend,
        
        @SerialName("temp")
        Temp,
    }
}