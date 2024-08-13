package org.tidepool.sdk.model.data

import com.google.gson.annotations.SerializedName

// schema bolus.v1
// line 2330
data class BolusData(
    val subType: BolusSubtype = BolusSubtype.normal,
    val deliveryContext: DeliveryContext,
) : BaseData(DataType.bolus) {
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}

enum class BolusSubtype {
    automated,
    
    @SerializedName("dual/square")
    dual_square,
    normal,
    square,
}

enum class DeliveryContext {
    device,
    algorithm,
    remote,
    undetermined
}