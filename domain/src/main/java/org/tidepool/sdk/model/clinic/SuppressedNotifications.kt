package org.tidepool.sdk.model.clinic

data class SuppressedNotifications(
    val dexcomDataOutdated: Boolean? = null,
    val dexcomDataStale: Boolean? = null,
    val initial: Boolean? = null,
    val patientDataOutdated: Boolean? = null,
)