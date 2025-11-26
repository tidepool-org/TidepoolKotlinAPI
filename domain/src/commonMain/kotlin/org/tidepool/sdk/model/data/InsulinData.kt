package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.time.Duration

// schema insulin.v1
data class InsulinData(
    override val id: String,
    override val type: DataType = DataType.Alert,
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
    val dose: Dose,
    val site: String?
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
    
    val formulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}