package org.tidepool.sdk.dto.clinic

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.Clinic
import org.tidepool.sdk.model.clinic.SuppressedNotifications

@Serializable
@KonvertTo(SuppressedNotifications::class, mapFunctionName = "toDomain")
data class SuppressedNotificationsDto(
    @SerialName("dexcomDataOutdated")
    val dexcomDataOutdated: Boolean? = null,
    @SerialName("dexcomDataStale")
    val dexcomDataStale: Boolean? = null,
    @SerialName("initial")
    val initial: Boolean? = null,
    @SerialName("patientDataOutdated")
    val patientDataOutdated: Boolean? = null,
) {
    @KonvertFrom(SuppressedNotifications::class, mapFunctionName = "fromDomain")
    companion object
}