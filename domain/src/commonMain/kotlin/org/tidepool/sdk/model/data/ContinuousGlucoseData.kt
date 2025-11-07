package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.BloodGlucose.GlucoseReading
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

data class ContinuousGlucoseData(
    override val id: String,
    override val type: DataType = DataType.Cbg,
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>> = emptyList(),
    override val associations: List<Association> = emptyList(),
    override val clockDriftOffset: Duration? = null,
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    override val timeZone: TimeZone? = null,
    override val timeZoneOffset: Int? = null,
    val value: Double? = null,
    val units: BloodGlucose.Units? = null,
    val trend: BloodGlucose.Trend? = null,
    val trendRate: Double? = null
) : BaseData(
    id = id,
    type = type,
    time = time,
    annotations = annotations,
    associations = associations,
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
) {
    
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
}