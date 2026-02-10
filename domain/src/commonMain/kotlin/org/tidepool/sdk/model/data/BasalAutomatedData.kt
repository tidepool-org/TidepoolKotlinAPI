package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.time.Duration

data class BasalAutomatedData(
    override val id: String,
    override val type: DataType = DataType.Basal,
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
    val deliveryType: DeliveryType,
    val duration: Double,
    val expectedDuration: Double? = null,
    val rate: Double = -1.0,
    val scheduleName: String? = null,
    val origin: Origin? = null,
    val payload: Payload? = null,
    val suppressed: Suppressed? = null,
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
    enum class DeliveryType {
        Automated,
        Scheduled,
        Suspend,
        Temp,
    }

    data class Origin(
        val id: String,
        val name: String,
        val type: String,
        val version: String,
    )

    data class Payload(
        val deliveredUnits: Double,
        val syncIdentifier: String,
    )

    data class Suppressed(
        val type: DataType = DataType.Basal,
        val deliveryType: DeliveryType,
        val rate: Double,
        val scheduleName: String? = null,
    )
}