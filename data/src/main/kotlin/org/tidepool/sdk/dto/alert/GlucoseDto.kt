package org.tidepool.sdk.dto.alert

import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.alert.Glucose

@Serializable(with = GlucoseDtoSerializer::class)
sealed class GlucoseDto {
    
    abstract val units: String
    
    @Serializable
    data class MgDLGlucoseDto(
        override val units: String = "mg/dL",
        val value: Int
    ) : GlucoseDto()
    
    @Serializable
    data class MmolGlucoseDto(
        override val units: String = "mmol/L",
        val value: Float
    ) : GlucoseDto()
}

internal fun Glucose.toDto(): GlucoseDto = when (this) {
    is Glucose.MgDLGlucose -> GlucoseDto.MgDLGlucoseDto(units, value)
    is Glucose.MmolGlucose -> GlucoseDto.MmolGlucoseDto(units, value)
}

internal fun GlucoseDto.toDomain(): Glucose = when (this) {
    is GlucoseDto.MgDLGlucoseDto -> Glucose.MgDLGlucose(units, value)
    is GlucoseDto.MmolGlucoseDto -> Glucose.MmolGlucose(units, value)
}