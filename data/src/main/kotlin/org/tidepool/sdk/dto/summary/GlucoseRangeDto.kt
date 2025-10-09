package org.tidepool.sdk.dto.summary

import kotlinx.serialization.Serializable

@Serializable
data class GlucoseRangeDto(
    val glucose: Double,
    val minutes: Int,
    val records: Int,
    val percent: Double,
    val variance: Double
)