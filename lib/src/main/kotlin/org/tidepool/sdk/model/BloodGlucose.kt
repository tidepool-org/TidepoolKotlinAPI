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
			return amount * units.value * value
		}
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