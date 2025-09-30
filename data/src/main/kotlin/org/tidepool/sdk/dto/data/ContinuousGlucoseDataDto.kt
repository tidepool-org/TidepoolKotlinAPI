package org.tidepool.sdk.dto.data

import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.BloodGlucoseDto.GlucoseReadingDto

@Serializable
data class ContinuousGlucoseDataDto(
    val value: Double? = null,
    val units: BloodGlucoseDto.UnitsDto? = null,
    val trend: BloodGlucoseDto.TrendDto? = null,
    val trendRate: Double? = null
) : BaseDataDto(type = DataTypeDto.Cbg) {
    
    public constructor(
        reading: GlucoseReadingDto?,
        trend: BloodGlucoseDto.TrendDto?,
        trendRate: Double?
    ) : this(reading?.amount, reading?.units, trend, trendRate)
    
    val reading: GlucoseReadingDto? by lazy {
        value?.let { value ->
            units?.let { units ->
                GlucoseReadingDto(value, units)
            }
        }
    }
    
    fun copy(
        reading: GlucoseReadingDto? = this.reading,
        trend: BloodGlucoseDto.TrendDto? = this.trend,
        trendRate: Double? = this.trendRate
    ) = copy(reading?.amount, reading?.units, trend, trendRate)
}