package org.tidepool.sdk.model.data

import io.mcarle.konvert.api.KonvertFrom
import org.tidepool.sdk.dto.data.InsulinDataDto

// schema insulin.v1
data class InsulinData(
    val dose: Dose,
    val site: String?
) : BaseData(DataType.Insulin) {
    
    @KonvertFrom(InsulinDataDto::class, mapFunctionName = "fromDto")
    companion object {}
    
    val formulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}