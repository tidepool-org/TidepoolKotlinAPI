package org.tidepool.sdk.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRequestDto(
    val grant_type: GrantTypeDto,
    val client_id: String,
    val client_secret: String? = null,
    val subject_token: String? = null,
    val subject_token_type: SubjectTokenTypeDto? = null,
    val requested_token_type: RequestedTokenTypeDto? = null,
    val subject_issuer: String? = null,
    val username: String? = null,
    val password: String? = null,
    val code: String? = null,
    val code_verifier: String? = null,
) {

    companion object {

        fun createWithAuthorizationCode(
            client_id: String,
            code: String,
            initializer: Builder.() -> Unit = {},
        ): TokenRequestDto {
            return Builder(GrantTypeDto.AuthorizationCode, client_id).apply {
                this.code = code
                initializer()
            }.build()
        }

        fun createWithRefreshToken(
            client_id: String,
            initializer: Builder.() -> Unit = {},
        ): TokenRequestDto {
            return Builder(GrantTypeDto.RefreshToken, client_id).apply {
                initializer()
            }.build()
        }

        fun createWithPassword(
            client_id: String,
            username: String,
            password: String,
            initializer: Builder.() -> Unit = {},
        ): TokenRequestDto {
            return Builder(GrantTypeDto.Password, client_id).apply {
                this.username = username
                this.password = password
                initializer()
            }.build()
        }

        fun createWithTokenExchange(
            client_id: String,
            subject_token: String,
            initializer: Builder.() -> Unit = {},
        ): TokenRequestDto {
            return Builder(GrantTypeDto.TokenExchange, client_id).apply {
                this.subject_token = subject_token
                initializer()
            }.build()
        }
    }
    
    class Builder(val grant_type: GrantTypeDto, val client_id: String) {

        var client_secret: String? = null
        var subject_token: String? = null
        var subject_token_type: SubjectTokenTypeDto? = null
        var requested_token_type: RequestedTokenTypeDto? = null
        var subject_issuer: String? = null
        var username: String? = null
        var password: String? = null
        var code: String? = null
        var code_verifier: String? = null

        fun build(): TokenRequestDto {
            return TokenRequestDto(
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
enum class GrantTypeDto {
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
enum class SubjectTokenTypeDto {

    @SerialName("urn:ietf:params:oauth:token-type:access_token")
    AccessToken,

    @SerialName("urn:ietf:params:oauth:token-type:jwt")
    Jwt
}

@Serializable
enum class RequestedTokenTypeDto {

    @SerialName("urn:ietf:params:oauth:token-type:access_token")
    access_token,

    @SerialName("urn:ietf:params:oauth:token-type:refresh_token")
    refresh_token
}