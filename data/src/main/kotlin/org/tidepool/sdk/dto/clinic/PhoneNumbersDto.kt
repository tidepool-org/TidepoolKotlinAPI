package org.tidepool.sdk.dto.clinic

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.Clinic
import org.tidepool.sdk.model.clinic.PhoneNumbers

@Serializable
@KonvertTo(PhoneNumbers::class, mapFunctionName = "toDomain")
data class PhoneNumbersDto(
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("fax")
    val fax: String? = null,
) {
    @KonvertFrom(PhoneNumbers::class, mapFunctionName = "fromDomain")
    companion object
}