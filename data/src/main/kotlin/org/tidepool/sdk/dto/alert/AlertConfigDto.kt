package org.tidepool.sdk.dto.alert

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.alert.AlertConfig
import org.tidepool.sdk.model.alert.ConnectionAlert
import org.tidepool.sdk.model.alert.GlucoseAlert
import org.tidepool.sdk.model.alert.UrgentGlucoseAlert

@Serializable
data class AlertConfigDto(
    @SerialName("uploadId")
    val uploadId: String? = null,
    @SerialName("userId")
    val userId: String? = null,
    @SerialName("followedUserId")
    val followedUserId: String? = null,
    @SerialName("urgentLow")
    val urgentLow: UrgentGlucoseAlertDto? = null,
    @SerialName("low")
    val low: GlucoseAlertDto? = null,
    @SerialName("high")
    val high: GlucoseAlertDto? = null,
    @SerialName("noCommunication")
    val noCommunication: ConnectionAlertDto? = null,
    @SerialName("notLooping")
    val notLooping: ConnectionAlertDto? = null,
)

@Serializable
data class UrgentGlucoseAlertDto(
    @SerialName("enabled")
    val enabled: Boolean = false,
    @SerialName("threshold")
    val threshold: GlucoseDto,
)

@Serializable
data class GlucoseAlertDto(
    @SerialName("enabled")
    val enabled: Boolean = false,
    @SerialName("threshold")
    val threshold: GlucoseDto,
    @SerialName("delay")
    val delayMinutes: Int = 0,
    @SerialName("repeat")
    val repeatMinutes: Int = 0,
)

@Serializable
@KonvertTo(ConnectionAlert::class, mapFunctionName = "toDomain")
data class ConnectionAlertDto(
    @SerialName("enabled")
    val enabled: Boolean = false,
    @SerialName("delay")
    val delayMinutes: Int = 0,
) {
    
    @KonvertFrom(ConnectionAlert::class, mapFunctionName = "fromDomain")
    companion object {}
}

// Manual mapping functions for AlertConfigDto
internal fun AlertConfigDto.toDomain(): AlertConfig = AlertConfig(
    uploadId = uploadId,
    userId = userId,
    followedUserId = followedUserId,
    urgentLow = urgentLow?.run { UrgentGlucoseAlert(enabled, threshold.toDomain()) },
    low = low?.run { GlucoseAlert(enabled, threshold.toDomain(), delayMinutes, repeatMinutes) },
    high = high?.run { GlucoseAlert(enabled, threshold.toDomain(), delayMinutes, repeatMinutes) },
    noCommunication = noCommunication?.toDomain(),
    notLooping = notLooping?.toDomain()
)

internal fun AlertConfig.toDto(): AlertConfigDto = AlertConfigDto(
    uploadId = uploadId,
    userId = userId,
    followedUserId = followedUserId,
    urgentLow = urgentLow?.run { UrgentGlucoseAlertDto(enabled, threshold.toDto()) },
    low = low?.run { GlucoseAlertDto(enabled, threshold.toDto(), delayMinutes, repeatMinutes) },
    high = high?.run { GlucoseAlertDto(enabled, threshold.toDto(), delayMinutes, repeatMinutes) },
    noCommunication = noCommunication?.let {
        ConnectionAlertDto.fromDomain(it)
    },
    notLooping = notLooping?.let {
        ConnectionAlertDto.fromDomain(it)
    },
)

