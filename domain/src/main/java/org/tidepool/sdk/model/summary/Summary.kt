package org.tidepool.sdk.model.summary

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