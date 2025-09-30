package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.BasalAutomatedDataDto
import org.tidepool.sdk.dto.data.BasalAutomatedDataDto.DeliveryTypeDto

// TODO: Finish implementing automated.v1
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
    
    enum class DeliveryType {
        Automated,
        Scheduled,
        Suspend,
        Temp,
    }
}

internal fun BasalAutomatedDataDto.toDomain() = BasalAutomatedData(
    deliveryType = deliveryType.toDomain(),
    duration = duration,
    expectedDuration = expectedDuration,
    rate = rate,
    scheduleName = scheduleName,
)

internal fun DeliveryTypeDto.toDomain(): BasalAutomatedData.DeliveryType = when (this) {
    DeliveryTypeDto.Automated -> BasalAutomatedData.DeliveryType.Automated
    DeliveryTypeDto.Scheduled -> BasalAutomatedData.DeliveryType.Scheduled
    DeliveryTypeDto.Suspend   -> BasalAutomatedData.DeliveryType.Suspend
    DeliveryTypeDto.Temp      -> BasalAutomatedData.DeliveryType.Temp
}