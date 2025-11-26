package org.tidepool.sdk.dto.summary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.summary.ContinuousPeriod

@Serializable
data class ContinuousRangesDto(
    @SerialName("realtime")
    val realtime: GlucoseRangeDto,
    @SerialName("deferred")
    val deferred: GlucoseRangeDto,
    @SerialName("total")
    val total: GlucoseRangeDto
)

@Serializable
data class ContinuousPeriodDto(
    @SerialName("realtime")
    val realtime: GlucoseRangeDto,
    @SerialName("deferred")
    val deferred: GlucoseRangeDto,
    @SerialName("total")
    val total: GlucoseRangeDto,
    @SerialName("averageDailyRecords")
    val averageDailyRecords: Double
)