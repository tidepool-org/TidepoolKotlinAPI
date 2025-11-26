package org.tidepool.sdk.model.summary

import kotlinx.datetime.Instant

data class SummaryDates(
    val lastUpdatedDate: Instant,
    val lastUpdatedReason: List<String>,
    val firstData: Instant,
    val lastData: Instant,
    val lastUploadDate: Instant,
    val outdatedSince: Instant? = null,
    val outdatedReason: List<String>
)