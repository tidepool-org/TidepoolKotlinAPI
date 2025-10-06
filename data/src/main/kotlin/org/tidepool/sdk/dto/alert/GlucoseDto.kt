package org.tidepool.sdk.dto.alert

import kotlinx.serialization.Serializable

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