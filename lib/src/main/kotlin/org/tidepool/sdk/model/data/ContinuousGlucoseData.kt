package org.tidepool.sdk.model.data

import io.mcarle.konvert.api.KonvertFrom
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.BloodGlucoseDto.TrendDto
import org.tidepool.sdk.dto.BloodGlucoseDto.UnitsDto
import org.tidepool.sdk.dto.data.ContinuousGlucoseDataDto
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.BloodGlucose.GlucoseReading

public data class ContinuousGlucoseData(
    val value: Double? = null,
    val units: BloodGlucose.Units? = null,
    val trend: BloodGlucose.Trend? = null,
    val trendRate: Double? = null
) : BaseData(type = DataType.Cbg) {
    
    // Note: Konvert annotation added but manual mapping kept due to complex enum transformations
    @KonvertFrom(ContinuousGlucoseDataDto::class, mapFunctionName = "fromDto")
    companion object {}
    
    // public constructor(
    //     reading: GlucoseReading?,
    //     trend: BloodGlucose.Trend?,
    //     trendRate: Double?
    // ) : this(reading?.amount, reading?.units, trend, trendRate)
    
    val reading: GlucoseReading? by lazy {
        value?.let { value ->
            units?.let { units ->
                GlucoseReading(value, units)
            }
        }
    }
    
    fun copy(
        reading: GlucoseReading? = this.reading,
        trend: BloodGlucose.Trend? = this.trend,
        trendRate: Double? = this.trendRate
    ) = copy(reading?.amount, reading?.units, trend, trendRate)
}