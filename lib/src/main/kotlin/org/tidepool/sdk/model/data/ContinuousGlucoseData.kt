package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.BloodGlucose.GlucoseReading

public data class ContinuousGlucoseData(
	val value: Double? = null,
	val units: BloodGlucose.Units? = null,
	val trend: BloodGlucose.Trend? = null,
	val trendRate: Double? = null
): BaseData(type=BaseData.DataType.cbg) {
	
	public constructor(reading: GlucoseReading?, trend: BloodGlucose.Trend?, trendRate: Double?) : this(reading?.amount, reading?.units, trend, trendRate)
	
	val reading: GlucoseReading? by lazy {
		value?.let { value ->
			units?.let { units ->
				GlucoseReading(value, units)
			}
		}
	}

	fun copy(reading: GlucoseReading? = this.reading, trend: BloodGlucose.Trend? = this.trend, trendRate: Double? = this.trendRate) = copy(reading?.amount, reading?.units, trend, trendRate)
}