package org.tidepool.sdk.auth;

import io.mcarle.konvert.api.KonvertFrom
import org.tidepool.sdk.dto.auth.TokenResponseDto

data class TokenResponse(
    val expiresIn: Int,
    val accessToken: String,
    val idToken: String,
    val tokenType: String,
    val refreshToken: String?
) {
    
    @KonvertFrom(TokenResponseDto::class, mapFunctionName = "fromDto")
    companion object {}
}