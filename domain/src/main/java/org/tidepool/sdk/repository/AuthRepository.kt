package org.tidepool.sdk.repository

import org.tidepool.sdk.model.auth.Realm

interface AuthRepository {

    // suspend fun obtainToken(
    //     realm: Realm,
    //     tokenRequest: TokenRequest,
    // ): Result<TokenResponse>

    suspend fun authorize(
        realm: Realm,
        clientId: String,
        scopes: Array<String>,
        redirectUri: String,
        loginHint: String? = null,
        kcIdpHint: String? = null,
        prompt: String? = null,
    ): Result<Unit>
}