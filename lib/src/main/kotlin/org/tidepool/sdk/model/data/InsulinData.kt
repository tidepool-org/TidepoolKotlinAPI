package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.InsulinDataDto

// schema insulin.v1
data class InsulinData(
    val dose: Dose,
    val site: String?
) : BaseData(DataType.Insulin) {
    
    val formulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}

internal fun InsulinDataDto.toDomain() = InsulinData(
    dose = dose.toDomain(),
    site = site,
)

internal fun InsulinData.toDto() = InsulinDataDto(
    dose = dose.toDto(),
    site = site,
)