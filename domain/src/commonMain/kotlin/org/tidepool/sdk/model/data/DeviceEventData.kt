package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.time.Duration

/**
 * Generic device event datum (e.g., CGM sensor/transmitter lifecycle events).
 */
data class DeviceEventData(
    override val id: String,
    override val type: DataType = DataType.DeviceEvent,
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>>? = null,
    override val associations: List<Association> = emptyList(),
    override val clockDriftOffset: Duration? = null,
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    override val timeZone: TimeZone? = null,
    override val timeZoneOffset: Int? = null,
    val subType: SubType,
    val deviceIdentifier: String? = null,
    /** seconds */
    val expectedLifetimeSeconds: Double? = null,
    /** seconds */
    val warmupPeriodSeconds: Double? = null,
    val failureMessage: String? = null,
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
    enum class SubType {
        SensorStart,
        SensorEnd,
        TransmitterStart,
        TransmitterEnd,
    }
}
