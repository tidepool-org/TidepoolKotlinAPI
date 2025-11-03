package org.tidepool.sdk.dto.clinic

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.PatientTag

@Serializable
@KonvertTo(PatientTag::class, mapFunctionName = "toDomain")
data class PatientTagDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
) {
    @KonvertFrom(PatientTag::class, mapFunctionName = "fromDomain")
    companion object
}