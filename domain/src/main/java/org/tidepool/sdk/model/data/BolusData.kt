package org.tidepool.sdk.model.data

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
