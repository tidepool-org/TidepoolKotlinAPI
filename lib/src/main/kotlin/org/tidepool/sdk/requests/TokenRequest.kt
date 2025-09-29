package org.tidepool.sdk.requests;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val grant_type: GrantType,
    val client_id: String,
    val client_secret: String? = null,
    val subject_token: String? = null,
    val subject_token_type: SubjectTokenType? = null,
    val requested_token_type: RequestedTokenType? = null,
    val subject_issuer: String? = null,
    val username: String? = null,
    val password: String? = null,
    val code: String? = null,
    val code_verifier: String? = null
) {
    
    companion object {
        
        fun createWithAuthorizationCode(
            client_id: String,
            code: String,
            initializer: Builder.() -> Unit = {}
        ): TokenRequest {
            return Builder(GrantType.AuthorizationCode, client_id).apply {
                this.code = code
                initializer()
            }.build()
        }
        
        fun createWithRefreshToken(
            client_id: String,
            initializer: Builder.() -> Unit = {}
        ): TokenRequest {
            return Builder(GrantType.RefreshToken, client_id).apply {
                initializer()
            }.build()
        }
        
        fun createWithPassword(
            client_id: String,
            username: String,
            password: String,
            initializer: Builder.() -> Unit = {}
        ): TokenRequest {
            return Builder(GrantType.Password, client_id).apply {
                this.username = username
                this.password = password
                initializer()
            }.build()
        }
        
        fun createWithTokenExchange(
            client_id: String,
            subject_token: String,
            initializer: Builder.() -> Unit = {}
        ): TokenRequest {
            return Builder(GrantType.TokenExchange, client_id).apply {
                this.subject_token = subject_token
                initializer()
            }.build()
        }
    }
    
    class Builder(val grant_type: GrantType, val client_id: String) {
        
        var client_secret: String? = null
        var subject_token: String? = null
        var subject_token_type: SubjectTokenType? = null
        var requested_token_type: RequestedTokenType? = null
        var subject_issuer: String? = null
        var username: String? = null
        var password: String? = null
        var code: String? = null
        var code_verifier: String? = null
        internal fun build(): TokenRequest {
            return TokenRequest(
                grant_type,
                client_id,
                client_secret,
                subject_token,
                subject_token_type,
                requested_token_type,
                subject_issuer,
                username,
                password,
                code,
                code_verifier
            )
        }
    }
}

@Serializable
enum class GrantType {
    @SerialName("authorization_code")
    AuthorizationCode,

    @SerialName("refresh_token")
    RefreshToken,

    @SerialName("password")
    Password,
    
    @SerialName("urn:ietf:params:oauth:grant-type:token-exchange")
    TokenExchange
}

@Serializable
enum class SubjectTokenType {
    
    @SerialName("urn:ietf:params:oauth:token-type:access_token")
    AccessToken,
    
    @SerialName("urn:ietf:params:oauth:token-type:jwt")
    Jwt
}

@Serializable
enum class RequestedTokenType {
    
    @SerialName("urn:ietf:params:oauth:token-type:access_token")
    access_token,
    
    @SerialName("urn:ietf:params:oauth:token-type:refresh_token")
    refresh_token
}