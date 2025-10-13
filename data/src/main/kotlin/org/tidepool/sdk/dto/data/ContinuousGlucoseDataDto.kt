package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.BloodGlucoseDto.GlucoseReadingDto
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.ContinuousGlucoseData

@Serializable
@KonvertTo(ContinuousGlucoseData::class, mapFunctionName = "toDomain")
data class ContinuousGlucoseDataDto(
    @SerialName("value")
    val value: Double? = null,
    @SerialName("units")
    val units: BloodGlucoseDto.UnitsDto? = null,
    @SerialName("trend")
    val trend: BloodGlucoseDto.TrendDto? = null,
    @SerialName("trendRate")
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