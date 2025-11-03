package org.tidepool.sdk.service

import org.tidepool.sdk.model.auth.Realm
import org.tidepool.sdk.model.auth.RequestedTokenType
import org.tidepool.sdk.model.auth.SubjectTokenType
import org.tidepool.sdk.model.auth.TokenRequest
import org.tidepool.sdk.model.auth.TokenResponse
import org.tidepool.sdk.repository.AuthenticationRepository

/**
 * Service for handling authentication operations including OAuth2 flows
 */
class AuthenticationService internal constructor(
    private val repository: AuthenticationRepository,
) {
    
    /**
     * Obtain access and refresh tokens using various OAuth2 grant types
     */
    suspend fun obtainToken(
        realm: Realm,
        tokenRequest: TokenRequest,
    ): Result<TokenResponse> = repository.obtainToken(
        realm = realm,
        tokenRequest = tokenRequest
    )
    
    /**
     * Initiate the OAuth2 authorization code flow
     * This typically redirects the user to a login page in a browser
     */
    suspend fun authorize(
        realm: Realm,
        clientId: String,
        scopes: Array<String>,
        redirectUri: String,
        loginHint: String? = null,
        kcIdpHint: String? = null,
        prompt: String? = null,
    ): Result<Unit> = repository.authorize(
        realm = realm,
        clientId = clientId,
        scopes = scopes,
        redirectUri = redirectUri,
        loginHint = loginHint,
        kcIdpHint = kcIdpHint,
        prompt = prompt
    )
    
    /**
     * Convenience method to obtain token using authorization code grant
     */
    suspend fun obtainTokenWithAuthorizationCode(
        realm: Realm,
        clientId: String,
        code: String,
        clientSecret: String? = null,
        codeVerifier: String? = null,
    ): Result<TokenResponse> {
        val tokenRequest = TokenRequest.withAuthorizationCode(clientId, code) {
            this.clientSecret = clientSecret
            this.codeVerifier = codeVerifier
        }
        return obtainToken(realm, tokenRequest)
    }
    
    /**
     * Convenience method to obtain token using refresh token grant
     */
    suspend fun obtainTokenWithRefreshToken(
        realm: Realm,
        clientId: String,
        clientSecret: String? = null,
    ): Result<TokenResponse> {
        val tokenRequest = TokenRequest.withRefreshToken(clientId) {
            this.clientSecret = clientSecret
        }
        return obtainToken(realm, tokenRequest)
    }
    
    /**
     * Convenience method to obtain token using password grant (trusted clients only)
     */
    suspend fun obtainTokenWithPassword(
        realm: Realm,
        clientId: String,
        username: String,
        password: String,
        clientSecret: String? = null,
    ): Result<TokenResponse> {
        val tokenRequest = TokenRequest.withPassword(clientId, username, password) {
            this.clientSecret = clientSecret
        }
        return obtainToken(realm, tokenRequest)
    }
    
    /**
     * Convenience method to obtain token using token exchange grant
     */
    suspend fun obtainTokenWithTokenExchange(
        realm: Realm,
        clientId: String,
        subjectToken: String,
        subjectTokenType: SubjectTokenType,
        requestedTokenType: RequestedTokenType,
        subjectIssuer: String,
        clientSecret: String? = null,
    ): Result<TokenResponse> {
        val tokenRequest = TokenRequest.withTokenExchange(clientId, subjectToken) {
            this.clientSecret = clientSecret
            this.subjectTokenType = subjectTokenType
            this.requestedTokenType = requestedTokenType
            this.subjectIssuer = subjectIssuer
        }
        return obtainToken(realm, tokenRequest)
    }
}