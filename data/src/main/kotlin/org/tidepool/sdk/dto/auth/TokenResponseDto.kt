package org.tidepool.sdk.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseDto(
    val expires_in: Int,
    val access_token: String,
    val id_token: String,
    val token_type: String,
    val refresh_token: String?
)