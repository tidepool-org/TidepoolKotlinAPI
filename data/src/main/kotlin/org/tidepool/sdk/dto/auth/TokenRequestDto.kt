package org.tidepool.sdk.dto.auth

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.auth.GrantType
import org.tidepool.sdk.model.auth.RequestedTokenType
import org.tidepool.sdk.model.auth.SubjectTokenType
import org.tidepool.sdk.model.auth.TokenRequest

@Serializable
data class TokenRequestDto(
    @SerialName("grant_type")
    val grantType: GrantTypeDto,
    @SerialName("client_id")
    val clientId: String,
    @SerialName("client_secret")
    val clientSecret: String? = null,
    @SerialName("subject_token")
    val subjectToken: String? = null,
    @SerialName("subject_token_type")
    val subjectTokenType: SubjectTokenTypeDto? = null,
    @SerialName("requested_token_type")
    val requestedTokenType: RequestedTokenTypeDto? = null,
    @SerialName("subject_issuer")
    val subjectIssuer: String? = null,
    @SerialName("username")
    val username: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("code_verifier")
    val codeVerifier: String? = null,
)

@Serializable
enum class GrantTypeDto {
    @SerialName("authorization_code")
    AuthorizationCode,

    @SerialName("refresh_token")
    RefreshToken,

    @SerialName("password")
    Password,

    @SerialName("urn:ietf:params:oauth:grant-type:token-exchange")
    TokenExchange,
    ;
}

@Serializable
enum class SubjectTokenTypeDto {

    @SerialName("urn:ietf:params:oauth:token-type:access_token")
    AccessToken,

    @SerialName("urn:ietf:params:oauth:token-type:jwt")
    Jwt,
    ;
}

@Serializable
enum class RequestedTokenTypeDto {

    @SerialName("urn:ietf:params:oauth:token-type:access_token")
    AccessToken,

    @SerialName("urn:ietf:params:oauth:token-type:refresh_token")
    RefreshToken,
    ;
}

internal fun GrantType.toDto() = when (this) {
    GrantType.AuthorizationCode -> GrantTypeDto.AuthorizationCode
    GrantType.RefreshToken      -> GrantTypeDto.RefreshToken
    GrantType.Password          -> GrantTypeDto.Password
    GrantType.TokenExchange     -> GrantTypeDto.TokenExchange
}

internal fun TokenRequest.toDto() = TokenRequestDto(
    grantType = grantType.toDto(),
    clientId = clientId,
    clientSecret = clientSecret,
    subjectToken = subjectToken,
    subjectTokenType = subjectTokenType?.toDto(),
)

internal fun SubjectTokenType.toDto() = when (this) {
    SubjectTokenType.AccessToken -> SubjectTokenTypeDto.AccessToken
    SubjectTokenType.Jwt         -> SubjectTokenTypeDto.Jwt
}

internal fun RequestedTokenType.toDto() = when (this) {
    RequestedTokenType.AccessToken  -> RequestedTokenTypeDto.AccessToken
    RequestedTokenType.RefreshToken -> RequestedTokenTypeDto.RefreshToken
}