package org.tidepool.sdk.dto.summary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.tidepool.sdk.model.summary.BgmSummary
import org.tidepool.sdk.model.summary.CgmSummary
import org.tidepool.sdk.model.summary.ContinuousPeriod
import org.tidepool.sdk.model.summary.ContinuousSummary
import org.tidepool.sdk.model.summary.Summary

@Serializable
@JsonClassDiscriminator("type")
sealed class SummaryDto {
    abstract val id: String
    abstract val type: SummaryTypeDto
    abstract val userId: String
    abstract val config: SummaryConfigDto
    abstract val dates: SummaryDatesDto
}

@Serializable
@SerialName("cgm")
data class CgmSummaryDto(
    @SerialName("id")
    override val id: String,
    @SerialName("type")
    override val type: SummaryTypeDto,
    @SerialName("userId")
    override val userId: String,
    @SerialName("config")
    override val config: SummaryConfigDto,
    @SerialName("dates")
    override val dates: SummaryDatesDto,
    @SerialName("periods")
    val periods: Map<String, GlucosePeriodDto>
) : SummaryDto()

@Serializable
@SerialName("bgm")
data class BgmSummaryDto(
    @SerialName("id")
    override val id: String,
    @SerialName("type")
    override val type: SummaryTypeDto,
    @SerialName("userId")
    override val userId: String,
    @SerialName("config")
    override val config: SummaryConfigDto,
    @SerialName("dates")
    override val dates: SummaryDatesDto,
    @SerialName("periods")
    val periods: Map<String, GlucosePeriodDto>
) : SummaryDto()

@Serializable
@SerialName("con")
data class ContinuousSummaryDto(
    @SerialName("id")
    override val id: String,
    @SerialName("type")
    override val type: SummaryTypeDto,
    @SerialName("userId")
    override val userId: String,
    @SerialName("config")
    override val config: SummaryConfigDto,
    @SerialName("dates")
    override val dates: SummaryDatesDto,
    @SerialName("periods")
    val periods: Map<String, ContinuousPeriodDto>
) : SummaryDto()

fun SummaryDto.toDomain(): Summary = when (this) {
    is CgmSummaryDto -> toDomain()
    is BgmSummaryDto -> toDomain()
    is ContinuousSummaryDto -> toDomain()
}

fun BgmSummaryDto.toDomain(): BgmSummary = BgmSummary(
    id = id,
    userId = userId,
    config = config.toDomain(),
    dates = dates.toDomain(),
    periods = periods.mapValues { (_, it) -> it.toDomain() }
)

fun CgmSummaryDto.toDomain(): CgmSummary = CgmSummary(
    id = id,
    userId = userId,
    config = config.toDomain(),
    dates = dates.toDomain(),
    periods = periods.mapValues { (_, it) -> it.toDomain() }
)

fun ContinuousPeriodDto.toDomain(): ContinuousPeriod = ContinuousPeriod(
    realtime = realtime.toDomain(),
    deferred = deferred.toDomain(),
    total = total.toDomain(),
    averageDailyRecords = averageDailyRecords
)

fun ContinuousSummaryDto.toDomain(): ContinuousSummary = ContinuousSummary(
    id = id,
    userId = userId,
    config = config.toDomain(),
    dates = dates.toDomain(),
    periods = periods.mapValues { (_, it) -> it.toDomain() }
)