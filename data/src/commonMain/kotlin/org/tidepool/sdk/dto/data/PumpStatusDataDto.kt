package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.Insulin
import org.tidepool.sdk.model.data.PumpStatusData
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** Mirrors TidepoolKit's TPumpStatusDatum. */
@Serializable
data class PumpStatusDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.PumpStatus,
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

    @SerialName("basalDelivery")
    val basalDelivery: BasalDeliveryDto? = null,
    @SerialName("battery")
    val battery: BatteryDto? = null,
    @SerialName("bolusDelivery")
    val bolusDelivery: BolusDeliveryDto? = null,
    @SerialName("deliveryIndeterminant")
    val deliveryIndeterminant: Boolean? = null,
    @SerialName("reservoir")
    val reservoir: ReservoirDto? = null,
    @SerialName("origin")
    val origin: OriginDto? = null,
    @SerialName("payload")
    val payload: PayloadDto? = null,
) : BaseDataDto() {

    @Serializable
    data class BasalDeliveryDto(
        @SerialName("state")
        val state: StateDto? = null,
        @Contextual
        @SerialName("time")
        val time: Instant? = null,
        @SerialName("dose")
        val dose: DoseDto? = null,
    ) {
        @Serializable
        enum class StateDto {
            @SerialName("scheduled")
            Scheduled,
            @SerialName("suspended")
            Suspended,
            @SerialName("temporary")
            Temporary,
            @SerialName("initiatingTemporary")
            InitiatingTemporary,
            @SerialName("cancelingTemporary")
            CancelingTemporary,
            @SerialName("suspending")
            Suspending,
            @SerialName("resuming")
            Resuming,
        }

        @Serializable
        data class DoseDto(
            @Contextual
            @SerialName("startTime")
            val startTime: Instant? = null,
            @Contextual
            @SerialName("endTime")
            val endTime: Instant? = null,
            @SerialName("rate")
            val rate: Double? = null,
            @SerialName("amountDelivered")
            val amountDelivered: Double? = null,
        )
    }

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
    data class BolusDeliveryDto(
        @SerialName("state")
        val state: StateDto? = null,
        @SerialName("dose")
        val dose: DoseDto? = null,
    ) {
        @Serializable
        enum class StateDto {
            @SerialName("none")
            None,
            @SerialName("initiating")
            Initiating,
            @SerialName("delivering")
            Delivering,
            @SerialName("canceling")
            Canceling,
        }

        @Serializable
        data class DoseDto(
            @Contextual
            @SerialName("startTime")
            val startTime: Instant? = null,
            @SerialName("amount")
            val amount: Double? = null,
            @SerialName("amountDelivered")
            val amountDelivered: Double? = null,
        )
    }

    @Serializable
    data class ReservoirDto(
        @Contextual
        @SerialName("time")
        val time: Instant? = null,
        @SerialName("remaining")
        val remaining: Double? = null,
        @SerialName("units")
        val units: InsulinDto.UnitsDto? = null,
    )

    @Serializable
    data class PayloadDto(
        @SerialName("syncIdentifier")
        val syncIdentifier: String,
    )
}

fun PumpStatusDataDto.toDomain(): PumpStatusData = PumpStatusData(
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
    basalDelivery = basalDelivery?.toDomain(),
    battery = battery?.toDomain(),
    bolusDelivery = bolusDelivery?.toDomain(),
    deliveryIndeterminant = deliveryIndeterminant,
    reservoir = reservoir?.toDomain(),
    origin = origin?.toDomain(),
    payload = payload?.toDomain(),
)

fun PumpStatusData.toDto(): PumpStatusDataDto = PumpStatusDataDto(
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
    basalDelivery = basalDelivery?.toDto(),
    battery = battery?.toDto(),
    bolusDelivery = bolusDelivery?.toDto(),
    deliveryIndeterminant = deliveryIndeterminant,
    reservoir = reservoir?.toDto(),
    origin = origin?.toDto(),
    payload = payload?.toDto(),
)

private fun PumpStatusDataDto.BasalDeliveryDto.StateDto.toDomain(): PumpStatusData.BasalDelivery.State = when (this) {
    PumpStatusDataDto.BasalDeliveryDto.StateDto.Scheduled -> PumpStatusData.BasalDelivery.State.Scheduled
    PumpStatusDataDto.BasalDeliveryDto.StateDto.Suspended -> PumpStatusData.BasalDelivery.State.Suspended
    PumpStatusDataDto.BasalDeliveryDto.StateDto.Temporary -> PumpStatusData.BasalDelivery.State.Temporary
    PumpStatusDataDto.BasalDeliveryDto.StateDto.InitiatingTemporary -> PumpStatusData.BasalDelivery.State.InitiatingTemporary
    PumpStatusDataDto.BasalDeliveryDto.StateDto.CancelingTemporary -> PumpStatusData.BasalDelivery.State.CancelingTemporary
    PumpStatusDataDto.BasalDeliveryDto.StateDto.Suspending -> PumpStatusData.BasalDelivery.State.Suspending
    PumpStatusDataDto.BasalDeliveryDto.StateDto.Resuming -> PumpStatusData.BasalDelivery.State.Resuming
}

private fun PumpStatusData.BasalDelivery.State.toDto(): PumpStatusDataDto.BasalDeliveryDto.StateDto = when (this) {
    PumpStatusData.BasalDelivery.State.Scheduled -> PumpStatusDataDto.BasalDeliveryDto.StateDto.Scheduled
    PumpStatusData.BasalDelivery.State.Suspended -> PumpStatusDataDto.BasalDeliveryDto.StateDto.Suspended
    PumpStatusData.BasalDelivery.State.Temporary -> PumpStatusDataDto.BasalDeliveryDto.StateDto.Temporary
    PumpStatusData.BasalDelivery.State.InitiatingTemporary -> PumpStatusDataDto.BasalDeliveryDto.StateDto.InitiatingTemporary
    PumpStatusData.BasalDelivery.State.CancelingTemporary -> PumpStatusDataDto.BasalDeliveryDto.StateDto.CancelingTemporary
    PumpStatusData.BasalDelivery.State.Suspending -> PumpStatusDataDto.BasalDeliveryDto.StateDto.Suspending
    PumpStatusData.BasalDelivery.State.Resuming -> PumpStatusDataDto.BasalDeliveryDto.StateDto.Resuming
}

private fun PumpStatusDataDto.BasalDeliveryDto.DoseDto.toDomain() = PumpStatusData.BasalDelivery.Dose(
    startTime = startTime,
    endTime = endTime,
    rate = rate,
    amountDelivered = amountDelivered,
)

private fun PumpStatusData.BasalDelivery.Dose.toDto() = PumpStatusDataDto.BasalDeliveryDto.DoseDto(
    startTime = startTime,
    endTime = endTime,
    rate = rate,
    amountDelivered = amountDelivered,
)

private fun PumpStatusDataDto.BasalDeliveryDto.toDomain() = PumpStatusData.BasalDelivery(
    state = state?.toDomain(),
    time = time,
    dose = dose?.toDomain(),
)

private fun PumpStatusData.BasalDelivery.toDto() = PumpStatusDataDto.BasalDeliveryDto(
    state = state?.toDto(),
    time = time,
    dose = dose?.toDto(),
)

private fun PumpStatusDataDto.BatteryDto.StateDto.toDomain(): PumpStatusData.Battery.State = when (this) {
    PumpStatusDataDto.BatteryDto.StateDto.Charging -> PumpStatusData.Battery.State.Charging
    PumpStatusDataDto.BatteryDto.StateDto.Full -> PumpStatusData.Battery.State.Full
    PumpStatusDataDto.BatteryDto.StateDto.Unplugged -> PumpStatusData.Battery.State.Unplugged
}

private fun PumpStatusData.Battery.State.toDto(): PumpStatusDataDto.BatteryDto.StateDto = when (this) {
    PumpStatusData.Battery.State.Charging -> PumpStatusDataDto.BatteryDto.StateDto.Charging
    PumpStatusData.Battery.State.Full -> PumpStatusDataDto.BatteryDto.StateDto.Full
    PumpStatusData.Battery.State.Unplugged -> PumpStatusDataDto.BatteryDto.StateDto.Unplugged
}

private fun PumpStatusDataDto.BatteryDto.UnitsDto.toDomain(): PumpStatusData.Battery.Units = when (this) {
    PumpStatusDataDto.BatteryDto.UnitsDto.Percent -> PumpStatusData.Battery.Units.Percent
}

private fun PumpStatusData.Battery.Units.toDto(): PumpStatusDataDto.BatteryDto.UnitsDto = when (this) {
    PumpStatusData.Battery.Units.Percent -> PumpStatusDataDto.BatteryDto.UnitsDto.Percent
}

private fun PumpStatusDataDto.BatteryDto.toDomain() = PumpStatusData.Battery(
    time = time,
    state = state?.toDomain(),
    remaining = remaining,
    units = units?.toDomain(),
)

private fun PumpStatusData.Battery.toDto() = PumpStatusDataDto.BatteryDto(
    time = time,
    state = state?.toDto(),
    remaining = remaining,
    units = units?.toDto(),
)

private fun PumpStatusDataDto.BolusDeliveryDto.StateDto.toDomain(): PumpStatusData.BolusDelivery.State = when (this) {
    PumpStatusDataDto.BolusDeliveryDto.StateDto.None -> PumpStatusData.BolusDelivery.State.None
    PumpStatusDataDto.BolusDeliveryDto.StateDto.Initiating -> PumpStatusData.BolusDelivery.State.Initiating
    PumpStatusDataDto.BolusDeliveryDto.StateDto.Delivering -> PumpStatusData.BolusDelivery.State.Delivering
    PumpStatusDataDto.BolusDeliveryDto.StateDto.Canceling -> PumpStatusData.BolusDelivery.State.Canceling
}

private fun PumpStatusData.BolusDelivery.State.toDto(): PumpStatusDataDto.BolusDeliveryDto.StateDto = when (this) {
    PumpStatusData.BolusDelivery.State.None -> PumpStatusDataDto.BolusDeliveryDto.StateDto.None
    PumpStatusData.BolusDelivery.State.Initiating -> PumpStatusDataDto.BolusDeliveryDto.StateDto.Initiating
    PumpStatusData.BolusDelivery.State.Delivering -> PumpStatusDataDto.BolusDeliveryDto.StateDto.Delivering
    PumpStatusData.BolusDelivery.State.Canceling -> PumpStatusDataDto.BolusDeliveryDto.StateDto.Canceling
}

private fun PumpStatusDataDto.BolusDeliveryDto.DoseDto.toDomain() = PumpStatusData.BolusDelivery.Dose(
    startTime = startTime,
    amount = amount,
    amountDelivered = amountDelivered,
)

private fun PumpStatusData.BolusDelivery.Dose.toDto() = PumpStatusDataDto.BolusDeliveryDto.DoseDto(
    startTime = startTime,
    amount = amount,
    amountDelivered = amountDelivered,
)

private fun PumpStatusDataDto.BolusDeliveryDto.toDomain() = PumpStatusData.BolusDelivery(
    state = state?.toDomain(),
    dose = dose?.toDomain(),
)

private fun PumpStatusData.BolusDelivery.toDto() = PumpStatusDataDto.BolusDeliveryDto(
    state = state?.toDto(),
    dose = dose?.toDto(),
)

private fun InsulinDto.UnitsDto.toDomain(): Insulin.Units = when (this) {
    InsulinDto.UnitsDto.Units -> Insulin.Units.Units
}

private fun Insulin.Units.toDto(): InsulinDto.UnitsDto = when (this) {
    Insulin.Units.Units -> InsulinDto.UnitsDto.Units
}

private fun PumpStatusDataDto.ReservoirDto.toDomain() = PumpStatusData.Reservoir(
    time = time,
    remaining = remaining,
    units = units?.toDomain(),
)

private fun PumpStatusData.Reservoir.toDto() = PumpStatusDataDto.ReservoirDto(
    time = time,
    remaining = remaining,
    units = units?.toDto(),
)

private fun PumpStatusDataDto.PayloadDto.toDomain() = PumpStatusData.Payload(
    syncIdentifier = syncIdentifier,
)

private fun PumpStatusData.Payload.toDto() = PumpStatusDataDto.PayloadDto(
    syncIdentifier = syncIdentifier,
)
