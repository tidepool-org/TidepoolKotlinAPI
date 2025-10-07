package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Serializable

@Serializable
data class SuppressedNotificationsDto(
    val dexcomDataOutdated: Boolean? = null,
    val dexcomDataStale: Boolean? = null,
    val initial: Boolean? = null,
    val patientDataOutdated: Boolean? = null,
)