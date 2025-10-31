package org.tidepool.sdk.dto.summary

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.summary.SummaryDates
import java.time.Instant

@Serializable
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

fun SummaryDatesDto.toDomain(): SummaryDates = SummaryDates(
    lastUpdatedDate = lastUpdatedDate,
    lastUpdatedReason = lastUpdatedReason,
    firstData = firstData,
    lastData = lastData,
    lastUploadDate = lastUploadDate,
    outdatedSince = outdatedSince,
    outdatedReason = outdatedReason,
)