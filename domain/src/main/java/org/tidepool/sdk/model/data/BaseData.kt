package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// TODO: finish implementing base.v1
sealed class BaseData(
    open val id: String,
    open val type: DataType = DataType.Alert,
    open val time: Instant? = null,
    open val annotations: List<Map<String, String>> = emptyList(),
    open val associations: List<Association> = emptyList(),
    open val clockDriftOffset: Duration? = null,
    open val conversionOffset: Duration? = null,
    open val dataSetId: String? = null,
    open val deviceTime: String? = null,
    open val notes: List<String> = emptyList(),
    open val timeZone: TimeZone? = null,
    open val timeZoneOffset: Duration? = null,
) {
    
    val location: Nothing
        get() = TODO("schema \"\" not implemented")
}