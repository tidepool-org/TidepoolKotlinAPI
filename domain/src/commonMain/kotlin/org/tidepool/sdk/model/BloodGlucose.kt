package org.tidepool.sdk.model

import org.tidepool.sdk.model.BloodGlucose.GlucoseReading
import org.tidepool.sdk.model.BloodGlucose.Units.MilligramsPerDeciliter
import org.tidepool.sdk.model.BloodGlucose.Units.MillimolesPerLiter
import java.time.Instant
import kotlin.math.roundToInt
import kotlin.time.Duration

public class BloodGlucose {
    public enum class Units(private val value: Double, val shorthand: String) {
        
        MilligramsPerDeciliter(18.018, "mg/dL"),
        
        MillimolesPerLiter(1.0, "mmol/L");
        
        fun convert(amount: Double, units: Units): Double {
            if (this == units) {
                return amount
            }
            return amount * units.value / value
        }
    }
    
    public class GlucoseReading(
        val amount: Double,
        val units: Units,
        val time: Instant? = null,
    ) : Comparable<GlucoseReading> {
        
        public fun inUnit(newUnit: Units): Double {
            return units.convert(amount, newUnit)
        }
        
        operator fun unaryPlus(): GlucoseReading = copy(amount = +this.amount)
        operator fun unaryMinus(): GlucoseReading = copy(amount = -this.amount)
        operator fun plus(other: GlucoseReading): GlucoseReading =
            copy(amount = amount + other.inUnit(units))
        
        operator fun minus(other: GlucoseReading): GlucoseReading =
            copy(amount = amount - other.inUnit(units))
        
        public fun copy(amount: Double = this.amount, units: Units = this.units) =
            GlucoseReading(amount, units)
        
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is GlucoseReading) return false
            return amount.equals(other.inUnit(units))
        }
        
        override fun hashCode(): Int {
            return inUnit(MilligramsPerDeciliter).hashCode()
        }
        
        override fun compareTo(other: GlucoseReading): Int {
            return amount.compareTo(other.inUnit(units))
        }
        
        private fun Double.roundMillimolesPerLiter(): Double {
            return (this * 10).roundToInt() / 10.0
        }
        
        fun toString(unit: Units): String {
            return when (unit) {
                MillimolesPerLiter     -> inUnit(unit).roundMillimolesPerLiter().toString()
                MilligramsPerDeciliter -> inUnit(unit).roundToInt().toString()
            }
        }
        
        fun toSignString(unit: Units): String {
            return when (unit) {
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
    }
    
    enum class Trend {
        Constant,
        SlowFall,
        SlowRise,
        ModerateFall,
        ModerateRise,
        RapidFall,
        RapidRise,
    }
    
    public data class Target(
        val target: Double?,
        val range: Double?,
        val low: Double?,
        val high: Double?
    )
    
    public data class StartTarget(
        val start: Duration?,
        val target: Double?,
        val range: Double?,
        val low: Double?,
        val high: Double?
    )
    
    companion object {
        
        private fun Units.valueRange() = when (this) {
            MilligramsPerDeciliter -> 0.0..1000.0
            MillimolesPerLiter     -> 0.0..55.0
        }
        
        fun clamp(value: Double, units: Units) = value.coerceIn(units.valueRange())
    }
}

public val Int.mgdl: GlucoseReading get() = GlucoseReading(toDouble(), MilligramsPerDeciliter)
public val Long.mgdl: GlucoseReading get() = GlucoseReading(toDouble(), MilligramsPerDeciliter)
public val Float.mgdl: GlucoseReading get() = GlucoseReading(toDouble(), MilligramsPerDeciliter)
public val Double.mgdl: GlucoseReading get() = GlucoseReading(this, MilligramsPerDeciliter)

public val Int.mmoll: GlucoseReading get() = GlucoseReading(toDouble(), MillimolesPerLiter)
public val Long.mmoll: GlucoseReading get() = GlucoseReading(toDouble(), MillimolesPerLiter)
public val Float.mmoll: GlucoseReading get() = GlucoseReading(toDouble(), MillimolesPerLiter)
public val Double.mmoll: GlucoseReading get() = GlucoseReading(this, MillimolesPerLiter)
