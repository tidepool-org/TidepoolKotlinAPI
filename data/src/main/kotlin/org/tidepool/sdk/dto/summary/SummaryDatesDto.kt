package org.tidepool.sdk.dto.summary

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class SummaryDatesDto(
    @Contextual val lastUpdatedDate: Instant,
    val lastUpdatedReason: List<String>,
    @Contextual val firstData: Instant,
    @Contextual val lastData: Instant,
    @Contextual val lastUploadDate: Instant,
    @Contextual val outdatedSince: Instant? = null,
    val outdatedReason: List<String> = emptyList(),
)