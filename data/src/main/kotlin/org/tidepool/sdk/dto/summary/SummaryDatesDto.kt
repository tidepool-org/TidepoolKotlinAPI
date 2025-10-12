package org.tidepool.sdk.dto.summary

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.summary.SummaryDates
import java.time.Instant

@Serializable
@KonvertTo(SummaryDates::class, mapFunctionName = "toDomain")
data class SummaryDatesDto(
    @SerialName("lastUpdatedDate")
    @Contextual
    val lastUpdatedDate: Instant,
    @SerialName("lastUpdatedReason")
    val lastUpdatedReason: List<String>,
    @SerialName("firstData")
    @Contextual
    val firstData: Instant,
    @SerialName("lastData")
    @Contextual
    val lastData: Instant,
    @SerialName("lastUploadDate")
    @Contextual
    val lastUploadDate: Instant,
    @SerialName("outdatedSince")
    @Contextual
    val outdatedSince: Instant? = null,
    @SerialName("outdatedReason")
    val outdatedReason: List<String> = emptyList(),
)