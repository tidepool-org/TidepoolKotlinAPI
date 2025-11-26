package org.tidepool.sdk

data class Session(
    val sessionToken: String,
    val refreshToken: String,
    val userId: String,
)