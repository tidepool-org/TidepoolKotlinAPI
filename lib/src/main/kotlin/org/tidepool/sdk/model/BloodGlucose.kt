package org.tidepool.sdk.model

import com.google.gson.annotations.SerializedName
import kotlin.time.Duration

public class BloodGlucose {
	public enum class Units {
		@SerializedName("mg/dL")
		milligramsPerDeciliter,
		@SerializedName("mmol/L")
		millimolesPerLiter
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
		fun valueRange(units: Units): ClosedRange<Double> {
			return when (units) {
				Units.milligramsPerDeciliter -> 0.0..1000.0
				Units.millimolesPerLiter -> 0.0..55.0
			}
		}
		fun clamp(value: Double, units: Units) {
			val range = valueRange(units)
			Math.min(Math.max(value, range.start), range.endInclusive)
		}
	}
}