package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.DoseDto
import org.tidepool.sdk.dto.data.InsulinDto

class Insulin {
    
    enum class Units {
        
        Units
    }
}

// schema dose.v1
data class Dose(
    val units: Insulin.Units,
    val total: Double,
    val food: Double?,
    val correction: Double?,
    val active: Double?,
)

internal fun DoseDto.toDomain() = Dose(
    units = units.toDomain(),
    total = total,
    food = food,
    correction = correction,
    active = active,
)

internal fun InsulinDto.UnitsDto.toDomain() = when (this) {
    InsulinDto.UnitsDto.Units -> Insulin.Units.Units
}

internal fun Dose.toDto() = DoseDto(
    units = units.toDto(),
    total = total,
    food = food,
    correction = correction,
    active = active,
)

internal fun Insulin.Units.toDto() = when (this) {
    Insulin.Units.Units -> InsulinDto.UnitsDto.Units
}