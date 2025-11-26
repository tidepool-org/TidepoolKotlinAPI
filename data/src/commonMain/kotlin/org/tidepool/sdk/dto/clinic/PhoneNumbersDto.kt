package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.PhoneNumbers

@Serializable
data class PhoneNumbersDto(
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("fax")
    val fax: String? = null,
)

fun PhoneNumbersDto.toDomain(): PhoneNumbers = PhoneNumbers(
    phone = phone,
    fax = fax
)

fun PhoneNumbers.toDto(): PhoneNumbersDto = PhoneNumbersDto(
    phone = phone,
    fax = fax
)