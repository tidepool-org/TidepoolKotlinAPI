package org.tidepool.sdk.dto.summary

import kotlinx.serialization.Serializable

@Serializable
data class ContinuousRangesDto(
    val realtime: GlucoseRangeDto,
    val deferred: GlucoseRangeDto,
    val total: GlucoseRangeDto
)

@Serializable
data class ContinuousPeriodDto(
    // From ContinuousRanges
    val realtime: GlucoseRangeDto,
    val deferred: GlucoseRangeDto,
    val total: GlucoseRangeDto,
    
    // Period-specific properties
    val averageDailyRecords: Double
)