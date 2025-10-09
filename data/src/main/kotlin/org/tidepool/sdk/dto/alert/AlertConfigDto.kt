package org.tidepool.sdk.dto.alert

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlertConfigDto(
    val uploadId: String? = null,
    val userId: String? = null,
    val followedUserId: String? = null,
    val urgentLow: UrgentGlucoseAlertDto? = null,
    val low: GlucoseAlertDto? = null,
    val high: GlucoseAlertDto? = null,
    val noCommunication: ConnectionAlertDto? = null,
    val notLooping: ConnectionAlertDto? = null,
)

@Serializable
data class UrgentGlucoseAlertDto(
    val enabled: Boolean = false,
    val threshold: GlucoseDto,
)

@Serializable
data class GlucoseAlertDto(
    val enabled: Boolean = false,
    val threshold: GlucoseDto,
    @SerialName("delay")
    val delayMinutes: Int = 0,
    @SerialName("repeat")
    val repeatMinutes: Int = 0,
)

@Serializable
data class ConnectionAlertDto(
    val enabled: Boolean = false,
    @SerialName("delay")
    val delayMinutes: Int = 0,
)
