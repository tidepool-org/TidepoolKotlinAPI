package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeDto(
    val key: String,
    val password: String,
    val email: String,
)
