package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.AuthenticationApi
import org.tidepool.sdk.dto.auth.TokenRequestDto
import org.tidepool.sdk.dto.auth.TokenResponseDto
import org.tidepool.sdk.dto.auth.RealmDto
import org.tidepool.sdk.repository.AuthenticationRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

class AuthenticationRepositoryImpl(
    private val authenticationApi: AuthenticationApi,
) : AuthenticationRepository {

    override suspend fun obtainToken(
        realm: RealmDto,
        tokenRequest: TokenRequestDto,
    ): Result<TokenResponseDto> = runCatchingNetworkExceptions {
        authenticationApi.obtainToken(
            realm = realm,
            grantType = tokenRequest
        )
    }

    override suspend fun authorize(
        realm: RealmDto,
        clientId: String,
        scopes: Array<String>,
        redirectUri: String,
        loginHint: String?,
        kcIdpHint: String?,
        prompt: String?,
    ): Result<Unit> = runCatchingNetworkExceptions {
        val scopeString = scopes.joinToString(" ")
        
        authenticationApi.authorize(
            realm = realm,
            clientId = clientId,
            scope = scopeString,
            responseType = "code",
            redirectUri = redirectUri,
            loginHint = loginHint,
            kcIdpHint = kcIdpHint,
            prompt = prompt
        )
        
        // The authorize endpoint typically redirects the user, so we return success
        Unit
    }
}