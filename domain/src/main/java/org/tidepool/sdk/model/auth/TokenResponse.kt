package org.tidepool.sdk.model.auth

data class TokenResponse(
    val expiresIn: Int,
    val accessToken: String,
    val idToken: String,
    val tokenType: String,
    val refreshToken: String?
)