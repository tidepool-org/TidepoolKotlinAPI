package org.tidepool.sdk.model.data

import kotlinx.serialization.Serializable

// schema insulin.v1
@Serializable
data class InsulinData(
    val dose: Dose,
    val site: String?
) : BaseData(DataType.insulin) {
    
    val formulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}