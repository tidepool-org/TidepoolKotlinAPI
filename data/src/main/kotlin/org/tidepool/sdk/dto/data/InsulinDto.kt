package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.Dose

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
@KonvertTo(Dose::class, mapFunctionName = "toDomain")
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
) {
    @KonvertFrom(Dose::class, mapFunctionName = "fromDomain")
    companion object {}
}
