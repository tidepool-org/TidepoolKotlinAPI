package org.tidepool.sdk.dto.data

import kotlinx.serialization.Serializable

@Serializable
class InsulinDto {
    
    @Serializable
    enum class UnitsDto {
        
        Units
    }
}

// schema dose.v1
@Serializable
data class DoseDto(
    val units: InsulinDto.UnitsDto,
    val total: Double,
    val food: Double?,
    val correction: Double?,
    val active: Double?,
)
