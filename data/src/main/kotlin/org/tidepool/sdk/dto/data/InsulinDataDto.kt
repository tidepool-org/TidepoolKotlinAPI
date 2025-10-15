package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.InsulinData

// schema insulin.v1
@Serializable
@KonvertTo(InsulinData::class, mapFunctionName = "toDomain")
data class InsulinDataDto(
    @SerialName("dose")
    val dose: DoseDto,
    @SerialName("site")
    val site: String?
) : BaseDataDto(DataTypeDto.Insulin) {
    
    val formulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}