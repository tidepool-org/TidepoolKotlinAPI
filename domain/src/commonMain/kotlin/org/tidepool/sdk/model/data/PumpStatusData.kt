package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** A pump's current delivery/battery/reservoir state. Mirrors TidepoolKit's TPumpStatusDatum. */
data class PumpStatusData(
    override val id: String,
    override val type: DataType = DataType.PumpStatus,
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
    val basalDelivery: BasalDelivery? = null,
    val battery: Battery? = null,
    val bolusDelivery: BolusDelivery? = null,
    val deliveryIndeterminant: Boolean? = null,
    val reservoir: Reservoir? = null,
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

    data class BasalDelivery(
        val state: State? = null,
        val time: Instant? = null,
        val dose: Dose? = null,
    ) {
        enum class State {
            Scheduled,
            Suspended,
            Temporary,
            InitiatingTemporary,
            CancelingTemporary,
            Suspending,
            Resuming,
        }

        data class Dose(
            val startTime: Instant? = null,
            val endTime: Instant? = null,
            val rate: Double? = null,
            val amountDelivered: Double? = null,
        )
    }

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

    data class BolusDelivery(
        val state: State? = null,
        val dose: Dose? = null,
    ) {
        enum class State {
            None,
            Initiating,
            Delivering,
            Canceling,
        }

        data class Dose(
            val startTime: Instant? = null,
            val amount: Double? = null,
            val amountDelivered: Double? = null,
        )
    }

    data class Reservoir(
        val time: Instant? = null,
        val remaining: Double? = null,
        val units: Insulin.Units? = null,
    )

    data class Payload(
        val syncIdentifier: String,
    )
}
