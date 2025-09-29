package org.tidepool.sdk.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: Finish implementing automated.v1
@Serializable
data class BasalAutomatedData(
    val deliveryType: DeliveryType,
    val duration: Int,
    val expectedDuration: Int? = null,
    val rate: Double = -1.0,
    val scheduleName: String? = null,
) : BaseData(DataType.Basal) {
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
    val suppressed: Nothing
        get() = TODO("schema \"scheduled.v1\" not implemented")
    
    @Serializable
    enum class DeliveryType {
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