package org.tidepool.sdk.model.summary

import org.tidepool.sdk.dto.summary.GlucoseRangeDto

data class GlucoseRange(
    val glucose: Double,
    val minutes: Int,
    val records: Int,
    val percent: Double,
    val variance: Double
)

internal fun GlucoseRange.toDto(): GlucoseRangeDto = GlucoseRangeDto(
    glucose = glucose,
    minutes = minutes,
    records = records,
    percent = percent,
    variance = variance
)

internal fun GlucoseRangeDto.toDomain(): GlucoseRange = GlucoseRange(
    glucose = glucose,
    minutes = minutes,
    records = records,
    percent = percent,
    variance = variance
)