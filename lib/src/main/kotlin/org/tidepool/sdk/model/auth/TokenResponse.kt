package org.tidepool.sdk.model.auth

import org.tidepool.sdk.dto.auth.TokenResponseDto

data class TokenResponse(
    val expiresIn: Int,
    val accessToken: String,
    val idToken: String,
    val tokenType: String,
    val refreshToken: String?
)

internal fun TokenResponseDto.toDomain() = TokenResponse(
    expiresIn = expires_in,
    accessToken = access_token,
    idToken = id_token,
    tokenType = token_type,
    refreshToken = refresh_token
)