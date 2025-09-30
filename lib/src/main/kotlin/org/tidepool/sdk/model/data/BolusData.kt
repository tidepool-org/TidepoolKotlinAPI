package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.BolusDataDto
import org.tidepool.sdk.dto.data.BolusSubtypeDto
import org.tidepool.sdk.dto.data.DeliveryContextDto

// schema bolus.v1
// line 2330
data class BolusData(
    val subType: BolusSubtype = BolusSubtype.Normal,
    val deliveryContext: DeliveryContext,
) : BaseData(DataType.Bolus) {
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}

enum class BolusSubtype {
    Automated,
    DualSquare,
    Normal,
    Square,
}

enum class DeliveryContext {
    Device,
    Algorithm,
    Remote,
    Undetermined
}

internal fun BolusDataDto.toDomain() = BolusData(
    subType = subType.toDomain(),
    deliveryContext = deliveryContext.toDomain()
)

internal fun DeliveryContextDto.toDomain(): DeliveryContext = when (this) {
    DeliveryContextDto.Device       -> DeliveryContext.Device
    DeliveryContextDto.Algorithm    -> DeliveryContext.Algorithm
    DeliveryContextDto.Remote       -> DeliveryContext.Remote
    DeliveryContextDto.Undetermined -> DeliveryContext.Undetermined
}

internal fun BolusSubtypeDto.toDomain(): BolusSubtype = when (this) {
    BolusSubtypeDto.Automated  -> BolusSubtype.Automated
    BolusSubtypeDto.DualSquare -> BolusSubtype.DualSquare
    BolusSubtypeDto.Normal     -> BolusSubtype.Normal
    BolusSubtypeDto.Square     -> BolusSubtype.Square
}