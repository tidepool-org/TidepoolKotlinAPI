package org.tidepool.sdk.dto.summary

import kotlinx.serialization.Serializable

@Serializable
data class GlucosePeriodDto(
    // From GlucoseRanges
    val total: GlucoseRangeDto,
    val inVeryLow: GlucoseRangeDto,
    val inLow: GlucoseRangeDto,
    val inTarget: GlucoseRangeDto,
    val inHigh: GlucoseRangeDto,
    val inVeryHigh: GlucoseRangeDto,
    val inExtremeHigh: GlucoseRangeDto,
    val inAnyLow: GlucoseRangeDto,
    val inAnyHigh: GlucoseRangeDto,
    
    // From GlucoseMinMax
    val min: Double,
    val minDelta: Double,
    val max: Double,
    val maxDelta: Double,
    
    // Period-specific properties
    val hoursWithData: Int,
    val daysWithData: Int,
    val averageGlucoseMmol: Double,
    val glucoseManagementIndicator: Double,
    val coefficientOfVariation: Double,
    val standardDeviation: Double,
    val averageDailyRecords: Double,
    val delta: GlucosePeriodDeltaDto? = null
)

@Serializable
data class GlucosePeriodDeltaDto(
    // From GlucoseRanges
    val total: GlucoseRangeDto,
    val inVeryLow: GlucoseRangeDto,
    val inLow: GlucoseRangeDto,
    val inTarget: GlucoseRangeDto,
    val inHigh: GlucoseRangeDto,
    val inVeryHigh: GlucoseRangeDto,
    val inExtremeHigh: GlucoseRangeDto,
    val inAnyLow: GlucoseRangeDto,
    val inAnyHigh: GlucoseRangeDto,
    
    // From GlucoseMinMax
    val min: Double,
    val minDelta: Double,
    val max: Double,
    val maxDelta: Double,
    
    // Period-specific properties
    val hoursWithData: Int,
    val daysWithData: Int,
    val averageGlucoseMmol: Double,
    val glucoseManagementIndicator: Double,
    val coefficientOfVariation: Double,
    val standardDeviation: Double,
    val averageDailyRecords: Double
)