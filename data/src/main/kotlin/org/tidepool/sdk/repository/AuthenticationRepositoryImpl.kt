package org.tidepool.sdk.repository

import org.tidepool.sdk.api.AuthenticationApi
import org.tidepool.sdk.dto.auth.TokenRequestDto
import org.tidepool.sdk.dto.auth.TokenResponseDto
import org.tidepool.sdk.dto.auth.RealmDto
import org.tidepool.sdk.dto.auth.fromDomain
import org.tidepool.sdk.dto.auth.toDomain
import org.tidepool.sdk.dto.auth.toDto
import org.tidepool.sdk.model.auth.Realm
import org.tidepool.sdk.model.auth.TokenRequest
import org.tidepool.sdk.model.auth.TokenResponse
import org.tidepool.sdk.runCatchingNetworkExceptions

class AuthenticationRepositoryImpl(
    private val authenticationApi: AuthenticationApi,
) : AuthenticationRepository {
    
    override suspend fun obtainToken(
        realm: Realm,
        tokenRequest: TokenRequest
    ) = runCatchingNetworkExceptions {
        authenticationApi.obtainToken(
            realm = realm.toDto().code,
            grantType = TokenRequestDto.fromDomain(tokenRequest),
        )
    }.map { it.toDomain() }
    
    override suspend fun authorize(
        realm: Realm,
        clientId: String,
        scopes: List<String>,
        redirectUri: String,
        loginHint: String?,
        kcIdpHint: String?,
        prompt: String?
    ) = runCatchingNetworkExceptions {
        val scopeString = scopes.joinToString(" ")
        
        authenticationApi.authorize(
            realm = realm.toDto().code,
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