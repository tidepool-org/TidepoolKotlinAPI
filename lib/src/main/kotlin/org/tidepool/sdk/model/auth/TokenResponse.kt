package org.tidepool.sdk.auth;

data class TokenResponse(
	val expires_in: Int,
	val access_token: String,
	val id_token: String,
	val token_type: String,
	val refresh_token: String?
)