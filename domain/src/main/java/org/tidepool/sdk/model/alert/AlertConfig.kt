package org.tidepool.sdk.model.alert

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