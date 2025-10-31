package org.tidepool.sdk.dto.summary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.summary.GlucosePeriod
import org.tidepool.sdk.model.summary.GlucosePeriodDelta

@Serializable
data class GlucosePeriodDto(
    // From GlucoseRanges
    @SerialName("total")
    val total: GlucoseRangeDto,
    @SerialName("inVeryLow")
    val inVeryLow: GlucoseRangeDto,
    @SerialName("inLow")
    val inLow: GlucoseRangeDto,
    @SerialName("inTarget")
    val inTarget: GlucoseRangeDto,
    @SerialName("inHigh")
    val inHigh: GlucoseRangeDto,
    @SerialName("inVeryHigh")
    val inVeryHigh: GlucoseRangeDto,
    @SerialName("inExtremeHigh")
    val inExtremeHigh: GlucoseRangeDto,
    @SerialName("inAnyLow")
    val inAnyLow: GlucoseRangeDto,
    @SerialName("inAnyHigh")
    val inAnyHigh: GlucoseRangeDto,
    
    // From GlucoseMinMax
    @SerialName("min")
    val min: Double,
    @SerialName("minDelta")
    val minDelta: Double,
    @SerialName("max")
    val max: Double,
    @SerialName("maxDelta")
    val maxDelta: Double,
    
    // Period-specific properties
    @SerialName("hoursWithData")
    val hoursWithData: Int,
    @SerialName("daysWithData")
    val daysWithData: Int,
    @SerialName("averageGlucoseMmol")
    val averageGlucoseMmol: Double,
    @SerialName("glucoseManagementIndicator")
    val glucoseManagementIndicator: Double,
    @SerialName("coefficientOfVariation")
    val coefficientOfVariation: Double,
    @SerialName("standardDeviation")
    val standardDeviation: Double,
    @SerialName("averageDailyRecords")
    val averageDailyRecords: Double,
    @SerialName("delta")
    val delta: GlucosePeriodDeltaDto? = null
)

@Serializable
data class GlucosePeriodDeltaDto(
    // From GlucoseRanges
    @SerialName("total")
    val total: GlucoseRangeDto,
    @SerialName("inVeryLow")
    val inVeryLow: GlucoseRangeDto,
    @SerialName("inLow")
    val inLow: GlucoseRangeDto,
    @SerialName("inTarget")
    val inTarget: GlucoseRangeDto,
    @SerialName("inHigh")
    val inHigh: GlucoseRangeDto,
    @SerialName("inVeryHigh")
    val inVeryHigh: GlucoseRangeDto,
    @SerialName("inExtremeHigh")
    val inExtremeHigh: GlucoseRangeDto,
    @SerialName("inAnyLow")
    val inAnyLow: GlucoseRangeDto,
    @SerialName("inAnyHigh")
    val inAnyHigh: GlucoseRangeDto,
    
    // From GlucoseMinMax
    @SerialName("min")
    val min: Double,
    @SerialName("minDelta")
    val minDelta: Double,
    @SerialName("max")
    val max: Double,
    @SerialName("maxDelta")
    val maxDelta: Double,
    
    // Period-specific properties
    @SerialName("hoursWithData")
    val hoursWithData: Int,
    @SerialName("daysWithData")
    val daysWithData: Int,
    @SerialName("averageGlucoseMmol")
    val averageGlucoseMmol: Double,
    @SerialName("glucoseManagementIndicator")
    val glucoseManagementIndicator: Double,
    @SerialName("coefficientOfVariation")
    val coefficientOfVariation: Double,
    @SerialName("standardDeviation")
    val standardDeviation: Double,
    @SerialName("averageDailyRecords")
    val averageDailyRecords: Double
)

fun GlucosePeriodDeltaDto.toDomain(): GlucosePeriodDelta = GlucosePeriodDelta(
    total = total.toDomain(),
    inVeryLow = inVeryLow.toDomain(),
    inLow = inLow.toDomain(),
    inTarget = inTarget.toDomain(),
    inHigh = inHigh.toDomain(),
    inVeryHigh = inVeryHigh.toDomain(),
    inExtremeHigh = inExtremeHigh.toDomain(),
    inAnyLow = inAnyLow.toDomain(),
    inAnyHigh = inAnyHigh.toDomain(),
    min = min,
    minDelta = minDelta,
    max = max,
    maxDelta = maxDelta,
    hoursWithData = hoursWithData,
    daysWithData = daysWithData,
    averageGlucoseMmol = averageGlucoseMmol,
    glucoseManagementIndicator = glucoseManagementIndicator,
    coefficientOfVariation = coefficientOfVariation,
    standardDeviation = standardDeviation,
    averageDailyRecords = averageDailyRecords
)

fun GlucosePeriodDto.toDomain(): GlucosePeriod = GlucosePeriod(
    total = total.toDomain(),
    inVeryLow = inVeryLow.toDomain(),
    inLow = inLow.toDomain(),
    inTarget = inTarget.toDomain(),
    inHigh = inHigh.toDomain(),
    inVeryHigh = inVeryHigh.toDomain(),
    inExtremeHigh = inExtremeHigh.toDomain(),
    inAnyLow = inAnyLow.toDomain(),
    inAnyHigh = inAnyHigh.toDomain(),
    min = min,
    minDelta = minDelta,
    max = max,
    maxDelta = maxDelta,
    hoursWithData = hoursWithData,
    daysWithData = daysWithData,
    averageGlucoseMmol = averageGlucoseMmol,
    glucoseManagementIndicator = glucoseManagementIndicator,
    coefficientOfVariation = coefficientOfVariation,
    standardDeviation = standardDeviation,
    averageDailyRecords = averageDailyRecords,
    delta = delta?.toDomain()
)
