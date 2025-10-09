package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumbersDto(
    val phone: String? = null,
    val fax: String? = null,
)