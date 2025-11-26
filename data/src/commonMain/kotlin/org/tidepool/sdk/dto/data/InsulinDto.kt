package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.Dose
import org.tidepool.sdk.model.data.Insulin

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

fun DoseDto.toDomain(): Dose = Dose(
    units = when (units) {
        InsulinDto.UnitsDto.Units -> Insulin.Units.Units
    },
    total = total,
    food = food,
    correction = correction,
    active = active
)

fun Dose.toDto(): DoseDto = DoseDto(
    units = when (units) {
        Insulin.Units.Units -> InsulinDto.UnitsDto.Units
    },
    total = total,
    food = food,
    correction = correction,
    active = active
)