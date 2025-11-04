package org.tidepool.sdk.repository

import org.tidepool.sdk.model.auth.Realm
import org.tidepool.sdk.model.auth.TokenRequest
import org.tidepool.sdk.model.auth.TokenResponse

interface AuthenticationRepository {
    
    suspend fun obtainToken(
        realm: Realm,
        tokenRequest: TokenRequest,
    ): Result<TokenResponse>
    
    suspend fun authorize(
        realm: Realm,
        clientId: String,
        scopes: List<String>,
        redirectUri: String,
        loginHint: String? = null,
        kcIdpHint: String? = null,
        prompt: String? = null,
    ): Result<Unit>
}