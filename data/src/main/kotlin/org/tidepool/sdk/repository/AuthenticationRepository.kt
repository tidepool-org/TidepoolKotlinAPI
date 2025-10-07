package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.auth.TokenRequestDto
import org.tidepool.sdk.dto.auth.TokenResponseDto
import org.tidepool.sdk.dto.auth.RealmDto

interface AuthenticationRepository {

    suspend fun obtainToken(
        realm: RealmDto,
        tokenRequest: TokenRequestDto,
    ): Result<TokenResponseDto>

    suspend fun authorize(
        realm: RealmDto,
        clientId: String,
        scopes: Array<String>,
        redirectUri: String,
        loginHint: String? = null,
        kcIdpHint: String? = null,
        prompt: String? = null,
    ): Result<Unit>
}