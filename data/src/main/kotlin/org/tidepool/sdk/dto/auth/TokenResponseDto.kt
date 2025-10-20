package org.tidepool.sdk.dto.auth

import io.mcarle.konvert.api.KonvertTo
import io.mcarle.konvert.api.Mapping
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.auth.TokenResponse

@Serializable
@KonvertTo(
    TokenResponse::class,
    mapFunctionName = "toDomain",
    mappings = [
        Mapping(
            target = "expiresAt",
            expression = "java.time.Instant.now() + java.time.Duration.ofSeconds(expiresIn.toLong())"
        )
    ]
)
data class TokenResponseDto(
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("id_token")
    val idToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("refresh_token")
    val refreshToken: String?,
)