package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.time.Duration
import kotlin.math.roundToLong
import kotlin.time.Duration.Companion.minutes

@Serializable
data class BasalAutomatedDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.Alert,
    @Contextual
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>>? = null,
    override val associations: List<AssociationDto> = emptyList(),
    @Contextual
    override val clockDriftOffset: Duration? = null,
    @Contextual
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    @Contextual
    override val timeZone: TimeZone? = null,
    @Contextual
    override val timeZoneOffset: Int? = null,
    
    @SerialName("deliveryType")
    val deliveryType: DeliveryTypeDto,
    @SerialName("duration")
    val duration: Long,
    @SerialName("expectedDuration")
    val expectedDuration: Long? = null,
    @SerialName("rate")
    val rate: Double = -1.0,
    @SerialName("scheduleName")
    val scheduleName: String? = null,
    @SerialName("origin")
    val origin: OriginDto? = null,
    @SerialName("payload")
    val payload: PayloadDto? = null,
    @SerialName("suppressed")
    val suppressed: SuppressedDto? = null,
) : BaseDataDto() {
    @Serializable
    data class OriginDto(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("type")
        val type: String,
        @SerialName("version")
        val version: String,
    )

    @Serializable
    data class PayloadDto(
        @SerialName("deliveredUnits")
        val deliveredUnits: Double,
        @SerialName("syncIdentifier")
        val syncIdentifier: String,
    )

    @Serializable
    data class SuppressedDto(
        @SerialName("type")
        val type: DataTypeDto = DataTypeDto.Basal,
        @SerialName("deliveryType")
        val deliveryType: DeliveryTypeDto,
        @SerialName("rate")
        val rate: Double,
        @SerialName("scheduleName")
        val scheduleName: String? = null,
    )

    @Serializable
    enum class DeliveryTypeDto {
        
        @SerialName("automated")
        Automated,
        
        @SerialName("scheduled")
        Scheduled,
        
        @SerialName("suspend")
        Suspend,
        
        @SerialName("temp")
        Temp,
    }
}

fun BasalAutomatedDataDto.toDomain(): BasalAutomatedData = BasalAutomatedData(
    id = id,
    type = this.type.toDomain(),
    time = time,
    annotations = annotations,
    associations = associations.map { it.toDomain() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    deliveryType = this.deliveryType.toDomain(),
    duration = duration.toDouble().div(1.minutes.inWholeNanoseconds),
    expectedDuration = expectedDuration?.toDouble()?.div(1.minutes.inWholeNanoseconds),
    rate = rate,
    scheduleName = scheduleName,
    origin = origin?.toDomain(),
    payload = payload?.toDomain(),
    suppressed = suppressed?.toDomain(),
)

fun BasalAutomatedDataDto.DeliveryTypeDto.toDomain(): BasalAutomatedData.DeliveryType = when (this) {
    BasalAutomatedDataDto.DeliveryTypeDto.Automated -> BasalAutomatedData.DeliveryType.Automated
    BasalAutomatedDataDto.DeliveryTypeDto.Scheduled -> BasalAutomatedData.DeliveryType.Scheduled
    BasalAutomatedDataDto.DeliveryTypeDto.Suspend -> BasalAutomatedData.DeliveryType.Suspend
    BasalAutomatedDataDto.DeliveryTypeDto.Temp -> BasalAutomatedData.DeliveryType.Temp
}

fun BasalAutomatedData.toDto(): BasalAutomatedDataDto = BasalAutomatedDataDto(
    id = id,
    type = type.toDto(),
    time = time,
    annotations = annotations?.takeUnless { it.isEmpty() },
    associations = associations.map { it.toDto() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    deliveryType = deliveryType.toDto(),
    duration = duration.times(1.minutes.inWholeMilliseconds).roundToLong(),
    expectedDuration = expectedDuration?.times(1.minutes.inWholeMilliseconds)?.roundToLong(),
    rate = rate,
    scheduleName = scheduleName,
    origin = origin?.toDto(),
    payload = payload?.toDto(),
    suppressed = suppressed?.toDto(),
)

fun BasalAutomatedData.DeliveryType.toDto(): BasalAutomatedDataDto.DeliveryTypeDto = when (this) {
    BasalAutomatedData.DeliveryType.Automated -> BasalAutomatedDataDto.DeliveryTypeDto.Automated
    BasalAutomatedData.DeliveryType.Scheduled -> BasalAutomatedDataDto.DeliveryTypeDto.Scheduled
    BasalAutomatedData.DeliveryType.Suspend -> BasalAutomatedDataDto.DeliveryTypeDto.Suspend
    BasalAutomatedData.DeliveryType.Temp -> BasalAutomatedDataDto.DeliveryTypeDto.Temp
}

private fun BasalAutomatedDataDto.OriginDto.toDomain(): BasalAutomatedData.Origin =
    BasalAutomatedData.Origin(
        id = id,
        name = name,
        type = type,
        version = version,
    )

private fun BasalAutomatedDataDto.PayloadDto.toDomain(): BasalAutomatedData.Payload =
    BasalAutomatedData.Payload(
        deliveredUnits = deliveredUnits,
        syncIdentifier = syncIdentifier,
    )

private fun BasalAutomatedDataDto.SuppressedDto.toDomain(): BasalAutomatedData.Suppressed =
    BasalAutomatedData.Suppressed(
        type = type.toDomain(),
        deliveryType = deliveryType.toDomain(),
        rate = rate,
        scheduleName = scheduleName,
    )

private fun BasalAutomatedData.Origin.toDto(): BasalAutomatedDataDto.OriginDto =
    BasalAutomatedDataDto.OriginDto(
        id = id,
        name = name,
        type = type,
        version = version,
    )

private fun BasalAutomatedData.Payload.toDto(): BasalAutomatedDataDto.PayloadDto =
    BasalAutomatedDataDto.PayloadDto(
        deliveredUnits = deliveredUnits,
        syncIdentifier = syncIdentifier,
    )

private fun BasalAutomatedData.Suppressed.toDto(): BasalAutomatedDataDto.SuppressedDto =
    BasalAutomatedDataDto.SuppressedDto(
        type = type.toDto(),
        deliveryType = deliveryType.toDto(),
        rate = rate,
        scheduleName = scheduleName,
    )
