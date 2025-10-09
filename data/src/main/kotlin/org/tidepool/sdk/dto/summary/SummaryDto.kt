package org.tidepool.sdk.dto.summary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

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
    override val id: String,
    override val type: SummaryTypeDto,
    override val userId: String,
    override val config: SummaryConfigDto,
    override val dates: SummaryDatesDto,
    val periods: Map<String, GlucosePeriodDto>
) : SummaryDto()

@Serializable
@SerialName("bgm")
data class BgmSummaryDto(
    override val id: String,
    override val type: SummaryTypeDto,
    override val userId: String,
    override val config: SummaryConfigDto,
    override val dates: SummaryDatesDto,
    val periods: Map<String, GlucosePeriodDto>
) : SummaryDto()

@Serializable
@SerialName("con")
data class ContinuousSummaryDto(
    override val id: String,
    override val type: SummaryTypeDto,
    override val userId: String,
    override val config: SummaryConfigDto,
    override val dates: SummaryDatesDto,
    val periods: Map<String, ContinuousPeriodDto>
) : SummaryDto()
