package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.BolusData

// schema bolus.v1
// line 2330
@Serializable
@KonvertTo(BolusData::class, mapFunctionName = "toDomain")
data class BolusDataDto(
    @SerialName("subType")
    val subType: BolusSubtypeDto = BolusSubtypeDto.Normal,
    @SerialName("deliveryContext")
    val deliveryContext: DeliveryContextDto,
) : BaseDataDto(DataTypeDto.Bolus) {
    
    @KonvertFrom(BolusData::class, mapFunctionName = "fromDomain")
    companion object {}
    
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