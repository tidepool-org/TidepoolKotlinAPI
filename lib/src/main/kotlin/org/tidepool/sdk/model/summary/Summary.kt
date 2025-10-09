package org.tidepool.sdk.model.summary

import org.tidepool.sdk.dto.summary.*

sealed class Summary {
    abstract val id: String
    abstract val userId: String
    abstract val config: SummaryConfig
    abstract val dates: SummaryDates
}

data class CgmSummary(
    override val id: String,
    override val userId: String,
    override val config: SummaryConfig,
    override val dates: SummaryDates,
    val periods: Map<String, GlucosePeriod> // CGM-specific glucose periods
) : Summary()

data class BgmSummary(
    override val id: String,
    override val userId: String,
    override val config: SummaryConfig,
    override val dates: SummaryDates,
    val periods: Map<String, GlucosePeriod> // BGM-specific glucose periods
) : Summary()

data class ContinuousSummary(
    override val id: String,
    override val userId: String,
    override val config: SummaryConfig,
    override val dates: SummaryDates,
    val periods: Map<String, ContinuousPeriod> // Continuous-specific periods
) : Summary()

// Domain models for the different period types
data class GlucosePeriod(
    val total: GlucoseRange,
    val inVeryLow: GlucoseRange,
    val inLow: GlucoseRange,
    val inTarget: GlucoseRange,
    val inHigh: GlucoseRange,
    val inVeryHigh: GlucoseRange,
    val inExtremeHigh: GlucoseRange,
    val inAnyLow: GlucoseRange,
    val inAnyHigh: GlucoseRange,
    val min: Double,
    val minDelta: Double,
    val max: Double,
    val maxDelta: Double,
    val hoursWithData: Int,
    val daysWithData: Int,
    val averageGlucoseMmol: Double,
    val glucoseManagementIndicator: Double,
    val coefficientOfVariation: Double,
    val standardDeviation: Double,
    val averageDailyRecords: Double,
    val delta: GlucosePeriodDelta? = null
)

data class GlucosePeriodDelta(
    val total: GlucoseRange,
    val inVeryLow: GlucoseRange,
    val inLow: GlucoseRange,
    val inTarget: GlucoseRange,
    val inHigh: GlucoseRange,
    val inVeryHigh: GlucoseRange,
    val inExtremeHigh: GlucoseRange,
    val inAnyLow: GlucoseRange,
    val inAnyHigh: GlucoseRange,
    val min: Double,
    val minDelta: Double,
    val max: Double,
    val maxDelta: Double,
    val hoursWithData: Int,
    val daysWithData: Int,
    val averageGlucoseMmol: Double,
    val glucoseManagementIndicator: Double,
    val coefficientOfVariation: Double,
    val standardDeviation: Double,
    val averageDailyRecords: Double
)

data class ContinuousPeriod(
    val realtime: GlucoseRange,
    val deferred: GlucoseRange,
    val total: GlucoseRange,
    val averageDailyRecords: Double
)

// Mapper functions for periods
internal fun GlucosePeriodDto.toDomain(): GlucosePeriod = GlucosePeriod(
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

internal fun GlucosePeriodDeltaDto.toDomain(): GlucosePeriodDelta = GlucosePeriodDelta(
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

internal fun ContinuousPeriodDto.toDomain(): ContinuousPeriod = ContinuousPeriod(
    realtime = realtime.toDomain(),
    deferred = deferred.toDomain(),
    total = total.toDomain(),
    averageDailyRecords = averageDailyRecords
)

internal fun GlucosePeriod.toDto(): GlucosePeriodDto = GlucosePeriodDto(
    total = total.toDto(),
    inVeryLow = inVeryLow.toDto(),
    inLow = inLow.toDto(),
    inTarget = inTarget.toDto(),
    inHigh = inHigh.toDto(),
    inVeryHigh = inVeryHigh.toDto(),
    inExtremeHigh = inExtremeHigh.toDto(),
    inAnyLow = inAnyLow.toDto(),
    inAnyHigh = inAnyHigh.toDto(),
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
    delta = delta?.toDto()
)

internal fun GlucosePeriodDelta.toDto(): GlucosePeriodDeltaDto = GlucosePeriodDeltaDto(
    total = total.toDto(),
    inVeryLow = inVeryLow.toDto(),
    inLow = inLow.toDto(),
    inTarget = inTarget.toDto(),
    inHigh = inHigh.toDto(),
    inVeryHigh = inVeryHigh.toDto(),
    inExtremeHigh = inExtremeHigh.toDto(),
    inAnyLow = inAnyLow.toDto(),
    inAnyHigh = inAnyHigh.toDto(),
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

internal fun ContinuousPeriod.toDto(): ContinuousPeriodDto = ContinuousPeriodDto(
    realtime = realtime.toDto(),
    deferred = deferred.toDto(),
    total = total.toDto(),
    averageDailyRecords = averageDailyRecords
)

// Main Summary mapper functions
internal fun Summary.toDto(): SummaryDto = when (this) {
    is CgmSummary -> CgmSummaryDto(
        id = id,
        type = SummaryType.Cgm.toDto(),
        userId = userId,
        config = config.toDto(),
        dates = dates.toDto(),
        periods = periods.mapValues { it.value.toDto() }
    )
    
    is BgmSummary -> BgmSummaryDto(
        id = id,
        type = SummaryType.Bgm.toDto(),
        userId = userId,
        config = config.toDto(),
        dates = dates.toDto(),
        periods = periods.mapValues { it.value.toDto() }
    )
    
    is ContinuousSummary -> ContinuousSummaryDto(
        id = id,
        type = SummaryType.Continuous.toDto(),
        userId = userId,
        config = config.toDto(),
        dates = dates.toDto(),
        periods = periods.mapValues { it.value.toDto() }
    )
}

internal fun SummaryDto.toDomain(): Summary = when (this) {
    is CgmSummaryDto -> CgmSummary(
        id = id,
        userId = userId,
        config = config.toDomain(),
        dates = dates.toDomain(),
        periods = periods.mapValues { it.value.toDomain() }
    )
    
    is BgmSummaryDto -> BgmSummary(
        id = id,
        userId = userId,
        config = config.toDomain(),
        dates = dates.toDomain(),
        periods = periods.mapValues { it.value.toDomain() }
    )
    
    is ContinuousSummaryDto -> ContinuousSummary(
        id = id,
        userId = userId,
        config = config.toDomain(),
        dates = dates.toDomain(),
        periods = periods.mapValues { it.value.toDomain() }
    )
}