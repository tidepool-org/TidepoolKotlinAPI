package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import org.tidepool.sdk.model.BloodGlucose
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** Self-monitored (fingerstick) blood glucose. Mirrors TidepoolKit's TSMBGDatum. */
data class SmbgData(
    override val id: String,
    override val type: DataType = DataType.Smbg,
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
    val value: Double? = null,
    val units: BloodGlucose.Units? = null,
    val subType: SubType? = null,
    /**
     * Where the reading came from. Uploading a datum with the same origin id replaces the
     * earlier one (the data set deduplicates by origin), and a queued deletion selects it by
     * that id.
     */
    val origin: Origin? = null,
    val payload: Payload? = null,
    val deviceId: String? = null,
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
        Manual,
        Linked,
    }

    data class Payload(
        val syncIdentifier: String,
        val syncVersion: Int? = null,
    )
}
