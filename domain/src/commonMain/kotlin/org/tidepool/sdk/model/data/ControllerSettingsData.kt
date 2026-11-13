package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** The controller's (phone's) settings. Mirrors TidepoolKit's TControllerSettingsDatum. */
data class ControllerSettingsData(
    override val id: String,
    override val type: DataType = DataType.ControllerSettings,
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
    val device: Device? = null,
    val notifications: Notifications? = null,
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

    data class Device(
        val firmwareVersion: String? = null,
        val hardwareVersion: String? = null,
        val manufacturers: List<String>? = null,
        val model: String? = null,
        val name: String? = null,
        val serialNumber: String? = null,
        val softwareVersion: String? = null,
    )

    data class Notifications(
        val authorization: Authorization? = null,
        val alert: Boolean? = null,
        val criticalAlert: Boolean? = null,
        val badge: Boolean? = null,
        val sound: Boolean? = null,
        val announcement: Boolean? = null,
        val timeSensitive: Boolean? = null,
        val scheduledDelivery: Boolean? = null,
        val notificationCenter: Boolean? = null,
        val lockScreen: Boolean? = null,
        val alertStyle: AlertStyle? = null,
    ) {
        enum class Authorization {
            NotDetermined,
            Denied,
            Authorized,
            Provisional,
            Ephemeral,
        }

        enum class AlertStyle {
            None,
            Alert,
            Banner,
        }
    }

    data class Payload(
        val syncIdentifier: String,
    )
}
