package org.tidepool.sdk.model.auth

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