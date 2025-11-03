package org.tidepool.sdk.dto.summary

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.summary.ContinuousPeriod
import org.tidepool.sdk.model.summary.GlucosePeriod

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
@KonvertTo(ContinuousPeriod::class, mapFunctionName = "toDomain")
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