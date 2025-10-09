package org.tidepool.sdk.model.auth

import org.tidepool.sdk.dto.auth.*

/**
 * Domain model for token request with various OAuth2 grant types
 */
data class TokenRequest private constructor(
    val grantType: GrantType,
    val clientId: String,
    val clientSecret: String? = null,
    val subjectToken: String? = null,
    val subjectTokenType: SubjectTokenType? = null,
    val requestedTokenType: RequestedTokenType? = null,
    val subjectIssuer: String? = null,
    val username: String? = null,
    val password: String? = null,
    val code: String? = null,
    val codeVerifier: String? = null,
) {
    
    companion object {
        
        fun withAuthorizationCode(
            clientId: String,
            code: String,
            initializer: Builder.() -> Unit = {},
        ): TokenRequest {
            return Builder(GrantType.AuthorizationCode, clientId).apply {
                this.code = code
                initializer()
            }.build()
        }
        
        fun withRefreshToken(
            clientId: String,
            initializer: Builder.() -> Unit = {},
        ): TokenRequest {
            return Builder(GrantType.RefreshToken, clientId).apply {
                initializer()
            }.build()
        }
        
        fun withPassword(
            clientId: String,
            username: String,
            password: String,
            initializer: Builder.() -> Unit = {},
        ): TokenRequest {
            return Builder(GrantType.Password, clientId).apply {
                this.username = username
                this.password = password
                initializer()
            }.build()
        }
        
        fun withTokenExchange(
            clientId: String,
            subjectToken: String,
            initializer: Builder.() -> Unit = {},
        ): TokenRequest {
            return Builder(GrantType.TokenExchange, clientId).apply {
                this.subjectToken = subjectToken
                initializer()
            }.build()
        }
    }
    
    class Builder(private val grantType: GrantType, private val clientId: String) {
        
        var clientSecret: String? = null
        var subjectToken: String? = null
        var subjectTokenType: SubjectTokenType? = null
        var requestedTokenType: RequestedTokenType? = null
        var subjectIssuer: String? = null
        var username: String? = null
        var password: String? = null
        var code: String? = null
        var codeVerifier: String? = null
        
        fun build(): TokenRequest {
            return TokenRequest(
                grantType,
                clientId,
                clientSecret,
                subjectToken,
                subjectTokenType,
                requestedTokenType,
                subjectIssuer,
                username,
                password,
                code,
                codeVerifier
            )
        }
    }
}

enum class GrantType {
    AuthorizationCode,
    RefreshToken,
    Password,
    TokenExchange
}

enum class SubjectTokenType {
    AccessToken,
    Jwt
}

enum class RequestedTokenType {
    AccessToken,
    RefreshToken
}

// Mappers
internal fun TokenRequest.toDto() = TokenRequestDto(
    grant_type = grantType.toDto(),
    client_id = clientId,
    client_secret = clientSecret,
    subject_token = subjectToken,
    subject_token_type = subjectTokenType?.toDto(),
    requested_token_type = requestedTokenType?.toDto(),
    subject_issuer = subjectIssuer,
    username = username,
    password = password,
    code = code,
    code_verifier = codeVerifier
)

internal fun GrantType.toDto() = when (this) {
    GrantType.AuthorizationCode -> GrantTypeDto.AuthorizationCode
    GrantType.RefreshToken      -> GrantTypeDto.RefreshToken
    GrantType.Password          -> GrantTypeDto.Password
    GrantType.TokenExchange     -> GrantTypeDto.TokenExchange
}

internal fun SubjectTokenType.toDto() = when (this) {
    SubjectTokenType.AccessToken -> SubjectTokenTypeDto.AccessToken
    SubjectTokenType.Jwt         -> SubjectTokenTypeDto.Jwt
}

internal fun RequestedTokenType.toDto() = when (this) {
    RequestedTokenType.AccessToken  -> RequestedTokenTypeDto.access_token
    RequestedTokenType.RefreshToken -> RequestedTokenTypeDto.refresh_token
}