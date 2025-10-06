package org.tidepool.sdk.model.alert

import org.tidepool.sdk.dto.alert.AlertConfigDto
import org.tidepool.sdk.dto.alert.ConnectionAlertDto
import org.tidepool.sdk.dto.alert.GlucoseAlertDto
import org.tidepool.sdk.dto.alert.UrgentGlucoseAlertDto

data class AlertConfig(
    val uploadId: String? = null,
    val userId: String? = null,
    val followedUserId: String? = null,
    val urgentLow: UrgentGlucoseAlert? = null,
    val low: GlucoseAlert? = null,
    val high: GlucoseAlert? = null,
    val noCommunication: ConnectionAlert? = null,
    val notLooping: ConnectionAlert? = null
)

data class UrgentGlucoseAlert(
    val enabled: Boolean = false,
    val threshold: Glucose
)

data class GlucoseAlert(
    val enabled: Boolean = false,
    val threshold: Glucose,
    val delayMinutes: Int = 0,
    val repeatMinutes: Int = 0,
)

data class ConnectionAlert(
    val enabled: Boolean = false,
    val delayMinutes: Int = 0,
)

// Conversion functions for main config
internal fun AlertConfig.toDto(): AlertConfigDto = AlertConfigDto(
    uploadId = uploadId,
    userId = userId,
    followedUserId = followedUserId,
    urgentLow = urgentLow?.toDto(),
    low = low?.toDto(),
    high = high?.toDto(),
    noCommunication = noCommunication?.toDto(),
    notLooping = notLooping?.toDto()
)

internal fun AlertConfigDto.toDomain() = AlertConfig(
    uploadId = uploadId,
    userId = userId,
    followedUserId = followedUserId,
    urgentLow = urgentLow?.toDomain(),
    low = low?.toDomain(),
    high = high?.toDomain(),
    noCommunication = noCommunication?.toDomain(),
    notLooping = notLooping?.toDomain()
)

// Individual alert conversion functions
internal fun UrgentGlucoseAlert.toDto() = UrgentGlucoseAlertDto(
    enabled = enabled,
    threshold = threshold.toDto()
)

internal fun UrgentGlucoseAlertDto.toDomain() = UrgentGlucoseAlert(
    enabled = enabled,
    threshold = threshold.toDomain()
)

internal fun GlucoseAlert.toDto() = GlucoseAlertDto(
    enabled = enabled,
    threshold = threshold.toDto(),
    delayMinutes = delayMinutes,
    repeatMinutes = repeatMinutes
)

internal fun GlucoseAlertDto.toDomain() = GlucoseAlert(
    enabled = enabled,
    threshold = threshold.toDomain(),
    delayMinutes = delayMinutes,
    repeatMinutes = repeatMinutes
)

internal fun ConnectionAlert.toDto() = ConnectionAlertDto(
    enabled = enabled,
    delayMinutes = delayMinutes
)

internal fun ConnectionAlertDto.toDomain() = ConnectionAlert(
    enabled = enabled,
    delayMinutes = delayMinutes
)