package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// schema bolus.v1
// line 2330
@Serializable
data class BolusDataDto(
    val subType: BolusSubtypeDto = BolusSubtypeDto.Normal,
    val deliveryContext: DeliveryContextDto,
) : BaseDataDto(DataTypeDto.Bolus) {
    
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
    Device,
    Algorithm,
    Remote,
    Undetermined,
    ;
}