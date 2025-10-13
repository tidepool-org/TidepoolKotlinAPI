package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// schema insulin.v1
@Serializable
data class InsulinDataDto(
    @SerialName("dose")
    val dose: DoseDto,
    @SerialName("site")
    val site: String?
) : BaseDataDto(DataTypeDto.Insulin) {
    
    val formulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}