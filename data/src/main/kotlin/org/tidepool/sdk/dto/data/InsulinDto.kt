package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class InsulinDto {
    
    @Serializable
    enum class UnitsDto {
        
        @SerialName("units")
        Units
    }
}

// schema dose.v1
@Serializable
data class DoseDto(
    @SerialName("units")
    val units: InsulinDto.UnitsDto,
    @SerialName("total")
    val total: Double,
    @SerialName("food")
    val food: Double?,
    @SerialName("correction")
    val correction: Double?,
    @SerialName("active")
    val active: Double?,
)
