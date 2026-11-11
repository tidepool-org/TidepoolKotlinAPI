package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// schema bolus.v1
// line 2330
data class BolusData(
    override val id: String,
    override val type: DataType = DataType.Bolus,
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
    val subType: BolusSubtype = BolusSubtype.Normal,
    val deliveryContext: DeliveryContext,
    val normal: Double? = null,
    /**
     * Where the bolus came from. Uploading a datum with the same origin id replaces the earlier
     * one, and a deletion selects it by that id (the data set deduplicates by origin).
     */
    val origin: Origin? = null,
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
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}

enum class BolusSubtype {
    Automated,
    DualSquare,
    Normal,
    Square,
}

enum class DeliveryContext {
    Device,
    Algorithm,
    Remote,
    Undetermined
}
