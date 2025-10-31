package org.tidepool.sdk.dto.summary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.summary.GlucosePeriod
import org.tidepool.sdk.model.summary.GlucoseRange

@Serializable
data class GlucoseRangeDto(
    @SerialName("glucose")
    val glucose: Double,
    @SerialName("minutes")
    val minutes: Int,
    @SerialName("records")
    val records: Int,
    @SerialName("percent")
    val percent: Double,
    @SerialName("variance")
    val variance: Double,
)

internal fun GlucoseRange.toDto(): GlucoseRangeDto = GlucoseRangeDto(
    glucose = glucose,
    minutes = minutes,
    records = records,
    percent = percent,
    variance = variance,
)

fun GlucoseRangeDto.toDomain(): GlucoseRange = GlucoseRange(
    glucose = glucose,
    minutes = minutes,
    records = records,
    percent = percent,
    variance = variance,
)