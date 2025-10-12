package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeDto(
    @SerialName("key")
    val key: String,
    @SerialName("password")
    val password: String,
    @SerialName("email")
    val email: String,
)
