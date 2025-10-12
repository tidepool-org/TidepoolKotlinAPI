package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.BasalAutomatedData

// TODO: Finish implementing automated.v1
@Serializable
@KonvertTo(BasalAutomatedData::class, mapFunctionName = "toDomain")
data class BasalAutomatedDataDto(
    @SerialName("deliveryType")
    val deliveryType: DeliveryTypeDto,
    @SerialName("duration")
    val duration: Int,
    @SerialName("expectedDuration")
    val expectedDuration: Int? = null,
    @SerialName("rate")
    val rate: Double = -1.0,
    @SerialName("scheduleName")
    val scheduleName: String? = null,
) : BaseDataDto(DataTypeDto.Basal) {
    
    @KonvertFrom(BasalAutomatedData::class, mapFunctionName = "fromDomain")
    companion object {}
    
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