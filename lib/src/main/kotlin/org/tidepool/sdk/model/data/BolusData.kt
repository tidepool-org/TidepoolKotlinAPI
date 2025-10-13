package org.tidepool.sdk.model.data

import io.mcarle.konvert.api.KonvertFrom
import org.tidepool.sdk.dto.data.BolusDataDto
import org.tidepool.sdk.dto.data.BolusSubtypeDto
import org.tidepool.sdk.dto.data.DeliveryContextDto

// schema bolus.v1
// line 2330
data class BolusData(
    val subType: BolusSubtype = BolusSubtype.Normal,
    val deliveryContext: DeliveryContext,
) : BaseData(DataType.Bolus) {
    
    @KonvertFrom(BolusDataDto::class, mapFunctionName = "fromDto")
    companion object {}
    
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
