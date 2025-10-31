package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.BolusData
import org.tidepool.sdk.model.data.BolusSubtype
import org.tidepool.sdk.model.data.DeliveryContext
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// schema bolus.v1
// line 2330
@Serializable
data class BolusDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.Alert,
    @Contextual
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>> = emptyList(),
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
    override val timeZoneOffset: Duration? = null,
    
    @SerialName("subType")
    val subType: BolusSubtypeDto = BolusSubtypeDto.Normal,
    @SerialName("deliveryContext")
    val deliveryContext: DeliveryContextDto,
) : BaseDataDto() {
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}

@Serializable
enum class BolusSubtypeDto {
    @SerialName("automated")
    Automated,
    @SerialName("dual/square")
    DualSquare,
    @SerialName("normal")
    Normal,
    @SerialName("square")
    Square,
    ;
}

@Serializable
enum class DeliveryContextDto {
    
    @SerialName("device")
    Device,
    
    @SerialName("algorithm")
    Algorithm,
    
    @SerialName("remote")
    Remote,
    
    @SerialName("undetermined")
    Undetermined,
    ;
}

fun BolusDataDto.toDomain(): BolusData = BolusData(
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
    deliveryContext = deliveryContext.toDomain(),
)

fun DeliveryContextDto.toDomain(): DeliveryContext = when (this) {
    DeliveryContextDto.Device -> DeliveryContext.Device
    DeliveryContextDto.Algorithm -> DeliveryContext.Algorithm
    DeliveryContextDto.Remote -> DeliveryContext.Remote
    DeliveryContextDto.Undetermined -> DeliveryContext.Undetermined
}

fun BolusSubtypeDto.toDomain(): BolusSubtype = when (this) {
    BolusSubtypeDto.Automated -> BolusSubtype.Automated
    BolusSubtypeDto.DualSquare -> BolusSubtype.DualSquare
    BolusSubtypeDto.Normal -> BolusSubtype.Normal
    BolusSubtypeDto.Square -> BolusSubtype.Square
}

fun BolusData.toDto(): BolusDataDto = BolusDataDto(
    id = id,
    type = type.toDto(),
    time = time,
    annotations = annotations,
    associations = associations.map { it.toDto() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    subType = subType.toDto(),
    deliveryContext = deliveryContext.toDto(),
)

fun DeliveryContext.toDto(): DeliveryContextDto = when (this) {
    DeliveryContext.Device -> DeliveryContextDto.Device
    DeliveryContext.Algorithm -> DeliveryContextDto.Algorithm
    DeliveryContext.Remote -> DeliveryContextDto.Remote
    DeliveryContext.Undetermined -> DeliveryContextDto.Undetermined
}

fun BolusSubtype.toDto(): BolusSubtypeDto = when (this) {
    BolusSubtype.Automated -> BolusSubtypeDto.Automated
    BolusSubtype.DualSquare -> BolusSubtypeDto.DualSquare
    BolusSubtype.Normal -> BolusSubtypeDto.Normal
    BolusSubtype.Square -> BolusSubtypeDto.Square
}