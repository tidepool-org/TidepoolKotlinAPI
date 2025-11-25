package org.tidepool.sdk.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.BloodGlucoseDto.GlucoseReadingDto
import org.tidepool.sdk.dto.BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter
import org.tidepool.sdk.dto.BloodGlucoseDto.UnitsDto.MillimolesPerLiter
import org.tidepool.sdk.dto.data.DosingDecisionDataDto
import org.tidepool.sdk.dto.data.toDomain
import org.tidepool.sdk.dto.data.toDto
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.data.DosingDecisionData
import java.time.Instant
import kotlin.math.roundToInt
import kotlin.time.Duration

class BloodGlucoseDto {
    @Serializable
    enum class UnitsDto(private val value: Double, val shorthand: String) {
        
        @SerialName("mg/dL")
        MilligramsPerDeciliter(18.018, "mg/dL"),
        
        @SerialName("mmol/L")
        MillimolesPerLiter(1.0, "mmol/L");
        
        fun convert(amount: Double, units: UnitsDto): Double {
            if (this == units) {
                return amount
            }
            return amount * units.value / value
        }
    }
    
    @Serializable
    class GlucoseReadingDto(
        @SerialName("amount")
        val amount: Double,
        @SerialName("units")
        val units: UnitsDto,
        @SerialName("time")
        @Contextual
        val time: Instant? = null,
    ) : Comparable<GlucoseReadingDto> {
        
        fun inUnit(newUnit: UnitsDto) = units.convert(amount, newUnit)
        
        operator fun unaryPlus(): GlucoseReadingDto = copy(amount = +this.amount)
        operator fun unaryMinus(): GlucoseReadingDto = copy(amount = -this.amount)
        operator fun plus(other: GlucoseReadingDto): GlucoseReadingDto =
            copy(amount = amount + other.inUnit(units))
        
        operator fun minus(other: GlucoseReadingDto): GlucoseReadingDto =
            copy(amount = amount - other.inUnit(units))
        
        fun copy(amount: Double = this.amount, units: UnitsDto = this.units) =
            GlucoseReadingDto(amount, units, time)
        
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is GlucoseReadingDto) return false
            return amount.equals(other.inUnit(units))
        }
        
        override fun hashCode() = inUnit(MilligramsPerDeciliter).hashCode()
        
        override fun compareTo(other: GlucoseReadingDto) = amount.compareTo(other.inUnit(units))
        
        private fun Double.roundMillimolesPerLiter() = (this * 10).roundToInt() / 10.0
        
        fun toString(unit: UnitsDto) = when (unit) {
            MillimolesPerLiter     -> inUnit(unit).roundMillimolesPerLiter().toString()
            MilligramsPerDeciliter -> inUnit(unit).roundToInt().toString()
        }
        
        fun toSignString(unit: UnitsDto) = when (unit) {
            MillimolesPerLiter     -> inUnit(unit).let {
                if (it == 0.0) "0" else "%+.1f".format(
                    it.roundMillimolesPerLiter()
                )
            }
            
            MilligramsPerDeciliter -> inUnit(unit).let { amount ->
                amount.roundToInt().let { if (it == 0) "0" else "%+d".format(it) }
            }
        }
    }
    
    @Serializable
    enum class TrendDto {
        
        @SerialName("constant")
        Constant,
        
        @SerialName("slowFall")
        SlowFall,
        
        @SerialName("slowRise")
        SlowRise,
        
        @SerialName("moderateFall")
        ModerateFall,
        
        @SerialName("moderateRise")
        ModerateRise,
        
        @SerialName("rapidFall")
        RapidFall,
        
        @SerialName("rapidRise")
        RapidRise,
    }
    
    @Serializable
    data class TargetDto(
        @SerialName("target")
        val target: Double?,
        @SerialName("range")
        val range: Double?,
        @SerialName("low")
        val low: Double?,
        @SerialName("high")
        val high: Double?
    )
    
    @Serializable
    data class StartTargetDto(
        @Contextual
        @SerialName("start")
        val start: Duration?,
        @SerialName("target")
        val target: Double?,
        @SerialName("range")
        val range: Double?,
        @SerialName("low")
        val low: Double?,
        @SerialName("high")
        val high: Double?,
    )
    
    companion object {
        
        private fun UnitsDto.valueRange(): ClosedRange<Double> {
            return when (this) {
                MilligramsPerDeciliter -> 0.0..1000.0
                MillimolesPerLiter     -> 0.0..55.0
            }
        }
        
        fun clamp(value: Double, units: UnitsDto): Double {
            return value.coerceIn(units.valueRange())
        }
    }
}

internal val Int.mgdlDto: GlucoseReadingDto
    get() = GlucoseReadingDto(
        toDouble(),
        MilligramsPerDeciliter
    )
internal val Long.mgdlDto: GlucoseReadingDto
    get() = GlucoseReadingDto(
        toDouble(),
        MilligramsPerDeciliter
    )
internal val Float.mgdlDto: GlucoseReadingDto
    get() = GlucoseReadingDto(
        toDouble(),
        MilligramsPerDeciliter
    )
internal val Double.mgdlDto: GlucoseReadingDto
    get() = GlucoseReadingDto(
        this,
        MilligramsPerDeciliter
    )

internal val Int.mmollDto: GlucoseReadingDto
    get() = GlucoseReadingDto(
        toDouble(),
        MillimolesPerLiter
    )
internal val Long.mmollDto: GlucoseReadingDto
    get() = GlucoseReadingDto(
        toDouble(),
        MillimolesPerLiter
    )
internal val Float.mmollDto: GlucoseReadingDto
    get() = GlucoseReadingDto(
        toDouble(),
        MillimolesPerLiter
    )
internal val Double.mmollDto: GlucoseReadingDto get() = GlucoseReadingDto(this, MillimolesPerLiter)

internal fun BloodGlucose.Units.toDto() = when (this) {
    BloodGlucose.Units.MilligramsPerDeciliter -> MilligramsPerDeciliter
    BloodGlucose.Units.MillimolesPerLiter     -> MillimolesPerLiter
}

fun BloodGlucose.Trend.toDto() = when (this) {
    BloodGlucose.Trend.Constant -> BloodGlucoseDto.TrendDto.Constant
    BloodGlucose.Trend.SlowFall -> BloodGlucoseDto.TrendDto.SlowFall
    BloodGlucose.Trend.SlowRise -> BloodGlucoseDto.TrendDto.SlowRise
    BloodGlucose.Trend.ModerateFall -> BloodGlucoseDto.TrendDto.ModerateFall
    BloodGlucose.Trend.ModerateRise -> BloodGlucoseDto.TrendDto.ModerateRise
    BloodGlucose.Trend.RapidFall -> BloodGlucoseDto.TrendDto.RapidFall
    BloodGlucose.Trend.RapidRise -> BloodGlucoseDto.TrendDto.RapidRise
}

fun BloodGlucoseDto.TrendDto.toDomain() = when (this) {
    BloodGlucoseDto.TrendDto.Constant -> BloodGlucose.Trend.Constant
    BloodGlucoseDto.TrendDto.SlowFall -> BloodGlucose.Trend.SlowFall
    BloodGlucoseDto.TrendDto.SlowRise -> BloodGlucose.Trend.SlowRise
    BloodGlucoseDto.TrendDto.ModerateFall -> BloodGlucose.Trend.ModerateFall
    BloodGlucoseDto.TrendDto.ModerateRise -> BloodGlucose.Trend.ModerateRise
    BloodGlucoseDto.TrendDto.RapidFall -> BloodGlucose.Trend.RapidFall
    BloodGlucoseDto.TrendDto.RapidRise -> BloodGlucose.Trend.RapidRise
}

fun DosingDecisionDataDto.UnitsDto.toDomain(): DosingDecisionData.Units = DosingDecisionData.Units(
    bg = bg.toDomain(),
    carb = carb.toDomain(),
    insulin = insulin.toDomain()
)

fun DosingDecisionData.Units.toDto() = DosingDecisionDataDto.UnitsDto(
    bg = bg.toDto(),
    carb = carb.toDto(),
    insulin = insulin.toDto()
)

fun BloodGlucose.GlucoseReading.toDto() = GlucoseReadingDto(
    amount = amount,
    units = units.toDto(),
    time = time,
)

fun GlucoseReadingDto.toDomain() = BloodGlucose.GlucoseReading(
    amount = amount,
    units = units.toDomain(),
    time = time,
)