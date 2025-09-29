package org.tidepool.sdk.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// schema bolus.v1
// line 2330
@Serializable
data class BolusData(
    val subType: BolusSubtype = BolusSubtype.normal,
    val deliveryContext: DeliveryContext,
) : BaseData(DataType.Bolus) {
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}

@Serializable
enum class BolusSubtype {
    automated,
    
    @SerialName("dual/square")
    dual_square,
    normal,
    square,
}

@Serializable
enum class DeliveryContext {
    device,
    algorithm,
    remote,
    undetermined
}