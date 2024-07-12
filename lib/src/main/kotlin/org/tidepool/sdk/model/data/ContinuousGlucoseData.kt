package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.BloodGlucose

public data class ContinuousGlucoseData(
	val value: Double? = null,
	val units: BloodGlucose.Units? = null,
	val trend: BloodGlucose.Trend? = null,
	val trendRate: Double? = null
): BaseData(type=BaseData.DataType.cbg)