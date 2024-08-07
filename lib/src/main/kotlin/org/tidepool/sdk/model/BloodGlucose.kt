package org.tidepool.sdk.model

import com.google.gson.annotations.SerializedName
import kotlin.time.Duration

public class BloodGlucose {
	public enum class Units(private val value: Double) {
		@SerializedName("mg/dL", alternate = ["mg/dl"])
		milligramsPerDeciliter(18.018),
		@SerializedName("mmol/L", alternate = ["mmol/l"])
		millimolesPerLiter(1.0);

		fun convert(amount: Double, units: Units): Double {
			if (this == units) {
				return amount
			}
			return amount * units.value / value
		}
	}

	public class GlucoseReading(val amount: Double, val units: Units): Comparable<GlucoseReading> {
		public fun inUnit(newUnit: Units): Double {
			return units.convert(amount, newUnit)
		}
		
		operator fun unaryPlus(): GlucoseReading = copy(amount = +this.amount)
		operator fun unaryMinus(): GlucoseReading = copy(amount = -this.amount)
		operator fun plus(other: GlucoseReading): GlucoseReading = copy(amount = amount + other.inUnit(units))
		operator fun minus(other: GlucoseReading): GlucoseReading = copy(amount = amount - other.inUnit(units))
		public fun copy(amount: Double = this.amount, units: Units = this.units) = GlucoseReading(amount, units)

		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is GlucoseReading) return false
			return amount.equals(other.inUnit(units))
		}

		override fun hashCode(): Int {
			return inUnit(Units.milligramsPerDeciliter).hashCode()
		}

  		override fun compareTo(other: GlucoseReading): Int {
			return amount.compareTo(other.inUnit(units))
		}
	}

	public fun Int.mgdl(): GlucoseReading {
		return GlucoseReading(toDouble(), Units.milligramsPerDeciliter)
	}

	public fun Long.mgdl(): GlucoseReading {
		return GlucoseReading(toDouble(), Units.milligramsPerDeciliter)
	}

	public fun Float.mgdl(): GlucoseReading {
		return GlucoseReading(toDouble(), Units.milligramsPerDeciliter)
	}

	public fun Double.mgdl(): GlucoseReading {
		return GlucoseReading(this, Units.milligramsPerDeciliter)
	}

	public fun Int.mmoll(): GlucoseReading {
		return GlucoseReading(toDouble(), Units.millimolesPerLiter)
	}

	public fun Long.mmoll(): GlucoseReading {
		return GlucoseReading(toDouble(), Units.millimolesPerLiter)
	}

	public fun Float.mmoll(): GlucoseReading {
		return GlucoseReading(toDouble(), Units.millimolesPerLiter)
	}

	public fun Double.mmoll(): GlucoseReading {
		return GlucoseReading(this, Units.millimolesPerLiter)
	}

	enum class Trend {
		constant,
		slowFall,
		slowRise,
		moderateFall,
		moderateRise,
		rapidFall,
		rapidRise
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
		private fun Units.valueRange(): ClosedRange<Double> {
			return when (this) {
				Units.milligramsPerDeciliter -> 0.0..1000.0
				Units.millimolesPerLiter -> 0.0..55.0
			}
		}
		fun clamp(value: Double, units: Units): Double {
			return value.coerceIn(units.valueRange())
		}
	}
}