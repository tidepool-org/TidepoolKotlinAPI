package org.tidepool.sdk.model.summary

import org.tidepool.sdk.dto.summary.SummaryDatesDto
import java.time.Instant

data class SummaryDates(
    val lastUpdatedDate: Instant,
    val lastUpdatedReason: List<String>,
    val firstData: Instant,
    val lastData: Instant,
    val lastUploadDate: Instant,
    val outdatedSince: Instant? = null,
    val outdatedReason: List<String>
)

internal fun SummaryDates.toDto(): SummaryDatesDto = SummaryDatesDto(
    lastUpdatedDate = lastUpdatedDate,
    lastUpdatedReason = lastUpdatedReason,
    firstData = firstData,
    lastData = lastData,
    lastUploadDate = lastUploadDate,
    outdatedSince = outdatedSince,
    outdatedReason = outdatedReason
)

internal fun SummaryDatesDto.toDomain(): SummaryDates = SummaryDates(
    lastUpdatedDate = lastUpdatedDate,
    lastUpdatedReason = lastUpdatedReason,
    firstData = firstData,
    lastData = lastData,
    lastUploadDate = lastUploadDate,
    outdatedSince = outdatedSince,
    outdatedReason = outdatedReason
)