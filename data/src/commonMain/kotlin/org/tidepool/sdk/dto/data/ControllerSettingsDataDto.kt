package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.ControllerSettingsData
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** Mirrors TidepoolKit's TControllerSettingsDatum. */
@Serializable
data class ControllerSettingsDataDto(
    override val id: String = "",
    override val type: BaseDataDto.DataTypeDto = BaseDataDto.DataTypeDto.ControllerSettings,
    @Contextual
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>>? = null,
    override val associations: List<AssociationDto> = emptyList(),
    @Contextual
    override val clockDriftOffset: Duration? = null,
    @Contextual
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    @Contextual
    @SerialName("timezone")
    override val timeZone: TimeZone? = null,
    @Contextual
    @SerialName("timezoneOffset")
    override val timeZoneOffset: Int? = null,

    @SerialName("device")
    val device: DeviceDto? = null,
    @SerialName("notifications")
    val notifications: NotificationsDto? = null,
    @SerialName("origin")
    val origin: OriginDto? = null,
    @SerialName("payload")
    val payload: PayloadDto? = null,
) : BaseDataDto() {

    @Serializable
    data class DeviceDto(
        @SerialName("firmwareVersion")
        val firmwareVersion: String? = null,
        @SerialName("hardwareVersion")
        val hardwareVersion: String? = null,
        @SerialName("manufacturers")
        val manufacturers: List<String>? = null,
        @SerialName("model")
        val model: String? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("serialNumber")
        val serialNumber: String? = null,
        @SerialName("softwareVersion")
        val softwareVersion: String? = null,
    )

    @Serializable
    data class NotificationsDto(
        @SerialName("authorization")
        val authorization: AuthorizationDto? = null,
        @SerialName("alert")
        val alert: Boolean? = null,
        @SerialName("criticalAlert")
        val criticalAlert: Boolean? = null,
        @SerialName("badge")
        val badge: Boolean? = null,
        @SerialName("sound")
        val sound: Boolean? = null,
        @SerialName("announcement")
        val announcement: Boolean? = null,
        @SerialName("timeSensitive")
        val timeSensitive: Boolean? = null,
        @SerialName("scheduledDelivery")
        val scheduledDelivery: Boolean? = null,
        @SerialName("notificationCenter")
        val notificationCenter: Boolean? = null,
        @SerialName("lockScreen")
        val lockScreen: Boolean? = null,
        @SerialName("alertStyle")
        val alertStyle: AlertStyleDto? = null,
    ) {
        @Serializable
        enum class AuthorizationDto {
            @SerialName("notDetermined")
            NotDetermined,
            @SerialName("denied")
            Denied,
            @SerialName("authorized")
            Authorized,
            @SerialName("provisional")
            Provisional,
            @SerialName("ephemeral")
            Ephemeral,
        }

        @Serializable
        enum class AlertStyleDto {
            @SerialName("none")
            None,
            @SerialName("alert")
            Alert,
            @SerialName("banner")
            Banner,
        }
    }

    @Serializable
    data class PayloadDto(
        @SerialName("syncIdentifier")
        val syncIdentifier: String,
    )
}

fun ControllerSettingsDataDto.toDomain(): ControllerSettingsData = ControllerSettingsData(
    id = id,
    type = type.toDomain(),
    time = time,
    annotations = annotations,
    associations = associations.map { it.toDomain() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    device = device?.toDomain(),
    notifications = notifications?.toDomain(),
    origin = origin?.toDomain(),
    payload = payload?.toDomain(),
)

fun ControllerSettingsData.toDto(): ControllerSettingsDataDto = ControllerSettingsDataDto(
    id = id,
    type = type.toDto(),
    time = time,
    annotations = annotations?.takeUnless { it.isEmpty() },
    associations = associations.map { it.toDto() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    device = device?.toDto(),
    notifications = notifications?.toDto(),
    origin = origin?.toDto(),
    payload = payload?.toDto(),
)

private fun ControllerSettingsDataDto.DeviceDto.toDomain() = ControllerSettingsData.Device(
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    manufacturers = manufacturers,
    model = model,
    name = name,
    serialNumber = serialNumber,
    softwareVersion = softwareVersion,
)

private fun ControllerSettingsData.Device.toDto() = ControllerSettingsDataDto.DeviceDto(
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    manufacturers = manufacturers,
    model = model,
    name = name,
    serialNumber = serialNumber,
    softwareVersion = softwareVersion,
)

private fun ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.toDomain(): ControllerSettingsData.Notifications.Authorization =
    when (this) {
        ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.NotDetermined ->
            ControllerSettingsData.Notifications.Authorization.NotDetermined
        ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.Denied ->
            ControllerSettingsData.Notifications.Authorization.Denied
        ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.Authorized ->
            ControllerSettingsData.Notifications.Authorization.Authorized
        ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.Provisional ->
            ControllerSettingsData.Notifications.Authorization.Provisional
        ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.Ephemeral ->
            ControllerSettingsData.Notifications.Authorization.Ephemeral
    }

private fun ControllerSettingsData.Notifications.Authorization.toDto(): ControllerSettingsDataDto.NotificationsDto.AuthorizationDto =
    when (this) {
        ControllerSettingsData.Notifications.Authorization.NotDetermined ->
            ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.NotDetermined
        ControllerSettingsData.Notifications.Authorization.Denied ->
            ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.Denied
        ControllerSettingsData.Notifications.Authorization.Authorized ->
            ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.Authorized
        ControllerSettingsData.Notifications.Authorization.Provisional ->
            ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.Provisional
        ControllerSettingsData.Notifications.Authorization.Ephemeral ->
            ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.Ephemeral
    }

private fun ControllerSettingsDataDto.NotificationsDto.AlertStyleDto.toDomain(): ControllerSettingsData.Notifications.AlertStyle =
    when (this) {
        ControllerSettingsDataDto.NotificationsDto.AlertStyleDto.None -> ControllerSettingsData.Notifications.AlertStyle.None
        ControllerSettingsDataDto.NotificationsDto.AlertStyleDto.Alert -> ControllerSettingsData.Notifications.AlertStyle.Alert
        ControllerSettingsDataDto.NotificationsDto.AlertStyleDto.Banner -> ControllerSettingsData.Notifications.AlertStyle.Banner
    }

private fun ControllerSettingsData.Notifications.AlertStyle.toDto(): ControllerSettingsDataDto.NotificationsDto.AlertStyleDto =
    when (this) {
        ControllerSettingsData.Notifications.AlertStyle.None -> ControllerSettingsDataDto.NotificationsDto.AlertStyleDto.None
        ControllerSettingsData.Notifications.AlertStyle.Alert -> ControllerSettingsDataDto.NotificationsDto.AlertStyleDto.Alert
        ControllerSettingsData.Notifications.AlertStyle.Banner -> ControllerSettingsDataDto.NotificationsDto.AlertStyleDto.Banner
    }

private fun ControllerSettingsDataDto.NotificationsDto.toDomain() = ControllerSettingsData.Notifications(
    authorization = authorization?.toDomain(),
    alert = alert,
    criticalAlert = criticalAlert,
    badge = badge,
    sound = sound,
    announcement = announcement,
    timeSensitive = timeSensitive,
    scheduledDelivery = scheduledDelivery,
    notificationCenter = notificationCenter,
    lockScreen = lockScreen,
    alertStyle = alertStyle?.toDomain(),
)

private fun ControllerSettingsData.Notifications.toDto() = ControllerSettingsDataDto.NotificationsDto(
    authorization = authorization?.toDto(),
    alert = alert,
    criticalAlert = criticalAlert,
    badge = badge,
    sound = sound,
    announcement = announcement,
    timeSensitive = timeSensitive,
    scheduledDelivery = scheduledDelivery,
    notificationCenter = notificationCenter,
    lockScreen = lockScreen,
    alertStyle = alertStyle?.toDto(),
)

private fun ControllerSettingsDataDto.PayloadDto.toDomain() = ControllerSettingsData.Payload(syncIdentifier = syncIdentifier)

private fun ControllerSettingsData.Payload.toDto() = ControllerSettingsDataDto.PayloadDto(syncIdentifier = syncIdentifier)
