package org.tidepool.sdk.repository

import org.tidepool.sdk.model.auth.Realm
<<<<<<<< HEAD:domain/src/main/java/org/tidepool/sdk/repository/AuthenticationRepository.kt
import org.tidepool.sdk.model.auth.TokenRequest
import org.tidepool.sdk.model.auth.TokenResponse
========
>>>>>>>> feature/NEMO-205/architecture_update:domain/src/main/java/org/tidepool/sdk/repository/AuthRepository.kt

interface AuthenticationRepository {

<<<<<<<< HEAD:domain/src/main/java/org/tidepool/sdk/repository/AuthenticationRepository.kt
    suspend fun obtainToken(
        realm: Realm,
        tokenRequest: TokenRequest,
    ): Result<TokenResponse>
========
    // suspend fun obtainToken(
    //     realm: Realm,
    //     tokenRequest: TokenRequest,
    // ): Result<TokenResponse>
>>>>>>>> feature/NEMO-205/architecture_update:domain/src/main/java/org/tidepool/sdk/repository/AuthRepository.kt

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