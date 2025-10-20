package org.tidepool.sdk.model.auth

import java.time.Instant

data class TokenResponse(
    val expiresAt: Instant,
    val accessToken: String,
    val idToken: String,
    val tokenType: String,
    val refreshToken: String?
)