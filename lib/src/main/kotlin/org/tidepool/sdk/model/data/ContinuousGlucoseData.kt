package org.tidepool.sdk.model.data

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

internal fun ContinuousGlucoseDataDto.toDomain() = ContinuousGlucoseData(
    value = this.value,
    units = when (units) {
        UnitsDto.MilligramsPerDeciliter -> BloodGlucose.Units.MilligramsPerDeciliter
        UnitsDto.MillimolesPerLiter     -> BloodGlucose.Units.MillimolesPerLiter
        null                            -> null
    },
    trend = when (trend) {
        TrendDto.Constant     -> BloodGlucose.Trend.Constant
        TrendDto.SlowFall     -> BloodGlucose.Trend.SlowFall
        TrendDto.SlowRise     -> BloodGlucose.Trend.SlowRise
        TrendDto.ModerateFall -> BloodGlucose.Trend.ModerateFall
        TrendDto.ModerateRise -> BloodGlucose.Trend.ModerateRise
        TrendDto.RapidFall    -> BloodGlucose.Trend.RapidFall
        TrendDto.RapidRise    -> BloodGlucose.Trend.RapidRise
        null                  -> null
    }
)

internal fun ContinuousGlucoseData.toDto() = ContinuousGlucoseDataDto(
    value = this.value,
    units = when (units) {
        BloodGlucose.Units.MilligramsPerDeciliter -> UnitsDto.MilligramsPerDeciliter
        BloodGlucose.Units.MillimolesPerLiter     -> UnitsDto.MillimolesPerLiter
        null                                      -> null
    },
    trend = when (trend) {
        BloodGlucose.Trend.Constant     -> TrendDto.Constant
        BloodGlucose.Trend.SlowFall     -> TrendDto.SlowFall
        BloodGlucose.Trend.SlowRise     -> TrendDto.SlowRise
        BloodGlucose.Trend.ModerateFall -> TrendDto.ModerateFall
        BloodGlucose.Trend.ModerateRise -> TrendDto.ModerateRise
        BloodGlucose.Trend.RapidFall    -> TrendDto.RapidFall
        BloodGlucose.Trend.RapidRise    -> TrendDto.RapidRise
        null                            -> null
    }
)
