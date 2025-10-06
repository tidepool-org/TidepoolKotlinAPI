package org.tidepool.sdk.model.alert

import org.tidepool.sdk.dto.alert.GlucoseDto

sealed class Glucose {
    
    abstract val units: String
    
    data class MgDLGlucose(
        override val units: String = "mg/dL",
        val value: Int
    ) : Glucose()
    
    data class MmolGlucose(
        override val units: String = "mmol/L",
        val value: Float
    ) : Glucose()
}

internal fun Glucose.toDto(): GlucoseDto = when (this) {
    is Glucose.MgDLGlucose -> GlucoseDto.MgDLGlucoseDto(units, value)
    is Glucose.MmolGlucose -> GlucoseDto.MmolGlucoseDto(units, value)
}

internal fun GlucoseDto.toDomain(): Glucose = when (this) {
    is GlucoseDto.MgDLGlucoseDto -> Glucose.MgDLGlucose(units, value)
    is GlucoseDto.MmolGlucoseDto -> Glucose.MmolGlucose(units, value)
}