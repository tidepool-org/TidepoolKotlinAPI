package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** The controller's (phone's) current battery state. Mirrors TidepoolKit's TControllerStatusDatum. */
data class ControllerStatusData(
    override val id: String,
    override val type: DataType = DataType.ControllerStatus,
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
    val battery: Battery? = null,
    val origin: Origin? = null,
    val payload: Payload? = null,
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

    data class Battery(
        val time: Instant? = null,
        val state: State? = null,
        val remaining: Double? = null,
        val units: Units? = null,
    ) {
        enum class State {
            Charging,
            Full,
            Unplugged,
        }

        enum class Units {
            Percent,
        }
    }

    data class Payload(
        val syncIdentifier: String,
    )
}
