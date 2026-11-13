package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import org.tidepool.sdk.model.BloodGlucose
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** A CGM's settings. Mirrors TidepoolKit's TCGMSettingsDatum. */
data class CgmSettingsData(
    override val id: String,
    override val type: DataType = DataType.CgmSettings,
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
    val firmwareVersion: String? = null,
    val hardwareVersion: String? = null,
    val manufacturers: List<String>? = null,
    val model: String? = null,
    val name: String? = null,
    val serialNumber: String? = null,
    val softwareVersion: String? = null,
    val transmitterId: String? = null,
    val units: BloodGlucose.Units? = null,
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

    data class Payload(
        val syncIdentifier: String,
    )
}
