package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.ControllerStatusData
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** Mirrors TidepoolKit's TControllerStatusDatum. */
@Serializable
data class ControllerStatusDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.ControllerStatus,
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
    @SerialName("timezone")
    override val timeZone: TimeZone? = null,
    @Contextual
    @SerialName("timezoneOffset")
    override val timeZoneOffset: Int? = null,

    @SerialName("battery")
    val battery: BatteryDto? = null,
    @SerialName("origin")
    val origin: OriginDto? = null,
    @SerialName("payload")
    val payload: PayloadDto? = null,
) : BaseDataDto() {

    @Serializable
    data class BatteryDto(
        @Contextual
        @SerialName("time")
        val time: Instant? = null,
        @SerialName("state")
        val state: StateDto? = null,
        @SerialName("remaining")
        val remaining: Double? = null,
        @SerialName("units")
        val units: UnitsDto? = null,
    ) {
        @Serializable
        enum class StateDto {
            @SerialName("charging")
            Charging,
            @SerialName("full")
            Full,
            @SerialName("unplugged")
            Unplugged,
        }

        @Serializable
        enum class UnitsDto {
            @SerialName("percent")
            Percent,
        }
    }

    @Serializable
    data class PayloadDto(
        @SerialName("syncIdentifier")
        val syncIdentifier: String,
    )
}

fun ControllerStatusDataDto.toDomain(): ControllerStatusData = ControllerStatusData(
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
    battery = battery?.toDomain(),
    origin = origin?.toDomain(),
    payload = payload?.toDomain(),
)

fun ControllerStatusData.toDto(): ControllerStatusDataDto = ControllerStatusDataDto(
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
    battery = battery?.toDto(),
    origin = origin?.toDto(),
    payload = payload?.toDto(),
)

private fun ControllerStatusDataDto.BatteryDto.StateDto.toDomain(): ControllerStatusData.Battery.State = when (this) {
    ControllerStatusDataDto.BatteryDto.StateDto.Charging -> ControllerStatusData.Battery.State.Charging
    ControllerStatusDataDto.BatteryDto.StateDto.Full -> ControllerStatusData.Battery.State.Full
    ControllerStatusDataDto.BatteryDto.StateDto.Unplugged -> ControllerStatusData.Battery.State.Unplugged
}

private fun ControllerStatusData.Battery.State.toDto(): ControllerStatusDataDto.BatteryDto.StateDto = when (this) {
    ControllerStatusData.Battery.State.Charging -> ControllerStatusDataDto.BatteryDto.StateDto.Charging
    ControllerStatusData.Battery.State.Full -> ControllerStatusDataDto.BatteryDto.StateDto.Full
    ControllerStatusData.Battery.State.Unplugged -> ControllerStatusDataDto.BatteryDto.StateDto.Unplugged
}

private fun ControllerStatusDataDto.BatteryDto.UnitsDto.toDomain(): ControllerStatusData.Battery.Units = when (this) {
    ControllerStatusDataDto.BatteryDto.UnitsDto.Percent -> ControllerStatusData.Battery.Units.Percent
}

private fun ControllerStatusData.Battery.Units.toDto(): ControllerStatusDataDto.BatteryDto.UnitsDto = when (this) {
    ControllerStatusData.Battery.Units.Percent -> ControllerStatusDataDto.BatteryDto.UnitsDto.Percent
}

private fun ControllerStatusDataDto.BatteryDto.toDomain() = ControllerStatusData.Battery(
    time = time,
    state = state?.toDomain(),
    remaining = remaining,
    units = units?.toDomain(),
)

private fun ControllerStatusData.Battery.toDto() = ControllerStatusDataDto.BatteryDto(
    time = time,
    state = state?.toDto(),
    remaining = remaining,
    units = units?.toDto(),
)

private fun ControllerStatusDataDto.PayloadDto.toDomain() = ControllerStatusData.Payload(
    syncIdentifier = syncIdentifier,
)

private fun ControllerStatusData.Payload.toDto() = ControllerStatusDataDto.PayloadDto(
    syncIdentifier = syncIdentifier,
)
