package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.SuppressedNotifications

@Serializable
data class SuppressedNotificationsDto(
    @SerialName("dexcomDataOutdated")
    val dexcomDataOutdated: Boolean? = null,
    @SerialName("dexcomDataStale")
    val dexcomDataStale: Boolean? = null,
    @SerialName("initial")
    val initial: Boolean? = null,
    @SerialName("patientDataOutdated")
    val patientDataOutdated: Boolean? = null,
)

fun SuppressedNotificationsDto.toDomain() = SuppressedNotifications(
    dexcomDataOutdated = dexcomDataOutdated,
    dexcomDataStale = dexcomDataStale,
    initial = initial,
    patientDataOutdated = patientDataOutdated
)

fun SuppressedNotifications.toDto() = SuppressedNotificationsDto(
    dexcomDataOutdated = dexcomDataOutdated,
    dexcomDataStale = dexcomDataStale,
    initial = initial,
    patientDataOutdated = patientDataOutdated
)