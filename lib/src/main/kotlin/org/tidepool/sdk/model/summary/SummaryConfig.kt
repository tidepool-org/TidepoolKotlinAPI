package org.tidepool.sdk.model.summary

import org.tidepool.sdk.dto.summary.SummaryConfigDto

data class SummaryConfig(
    val schemaVersion: Int,
    val highGlucoseThreshold: Double,
    val veryHighGlucoseThreshold: Double,
    val lowGlucoseThreshold: Double,
    val veryLowGlucoseThreshold: Double
)

internal fun SummaryConfig.toDto(): SummaryConfigDto = SummaryConfigDto(
    schemaVersion = schemaVersion,
    highGlucoseThreshold = highGlucoseThreshold,
    veryHighGlucoseThreshold = veryHighGlucoseThreshold,
    lowGlucoseThreshold = lowGlucoseThreshold,
    veryLowGlucoseThreshold = veryLowGlucoseThreshold
)

internal fun SummaryConfigDto.toDomain(): SummaryConfig = SummaryConfig(
    schemaVersion = schemaVersion,
    highGlucoseThreshold = highGlucoseThreshold,
    veryHighGlucoseThreshold = veryHighGlucoseThreshold,
    lowGlucoseThreshold = lowGlucoseThreshold,
    veryLowGlucoseThreshold = veryLowGlucoseThreshold
)