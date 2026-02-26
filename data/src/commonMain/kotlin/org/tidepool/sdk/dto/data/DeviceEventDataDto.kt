package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.DeviceEventData
import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.time.Duration

@Serializable
data class DeviceEventDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.DeviceEvent,
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

    @SerialName("subType")
    val subType: SubTypeDto,
    @SerialName("deviceIdentifier")
    val deviceIdentifier: String? = null,
    @SerialName("expectedLifetimeSeconds")
    val expectedLifetimeSeconds: Double? = null,
    @SerialName("warmupPeriodSeconds")
    val warmupPeriodSeconds: Double? = null,
    @SerialName("failureMessage")
    val failureMessage: String? = null,
) : BaseDataDto() {

    @Serializable
    enum class SubTypeDto {
        @SerialName("sensorStart")
        SensorStart,

        @SerialName("sensorEnd")
        SensorEnd,

        @SerialName("transmitterStart")
        TransmitterStart,

        @SerialName("transmitterEnd")
        TransmitterEnd,
    }
}

internal fun DeviceEventDataDto.SubTypeDto.toDomain(): DeviceEventData.SubType = when (this) {
    DeviceEventDataDto.SubTypeDto.SensorStart -> DeviceEventData.SubType.SensorStart
    DeviceEventDataDto.SubTypeDto.SensorEnd -> DeviceEventData.SubType.SensorEnd
    DeviceEventDataDto.SubTypeDto.TransmitterStart -> DeviceEventData.SubType.TransmitterStart
    DeviceEventDataDto.SubTypeDto.TransmitterEnd -> DeviceEventData.SubType.TransmitterEnd
}

internal fun DeviceEventData.SubType.toDto(): DeviceEventDataDto.SubTypeDto = when (this) {
    DeviceEventData.SubType.SensorStart -> DeviceEventDataDto.SubTypeDto.SensorStart
    DeviceEventData.SubType.SensorEnd -> DeviceEventDataDto.SubTypeDto.SensorEnd
    DeviceEventData.SubType.TransmitterStart -> DeviceEventDataDto.SubTypeDto.TransmitterStart
    DeviceEventData.SubType.TransmitterEnd -> DeviceEventDataDto.SubTypeDto.TransmitterEnd
}

fun DeviceEventDataDto.toDomain(): DeviceEventData = DeviceEventData(
    id = id,
    type = type.toDomain(),
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
    subType = subType.toDomain(),
    deviceIdentifier = deviceIdentifier,
    expectedLifetimeSeconds = expectedLifetimeSeconds,
    warmupPeriodSeconds = warmupPeriodSeconds,
    failureMessage = failureMessage,
)

fun DeviceEventData.toDto(): DeviceEventDataDto = DeviceEventDataDto(
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
    subType = subType.toDto(),
    deviceIdentifier = deviceIdentifier,
    expectedLifetimeSeconds = expectedLifetimeSeconds,
    warmupPeriodSeconds = warmupPeriodSeconds,
    failureMessage = failureMessage,
)
