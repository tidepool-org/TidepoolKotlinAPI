package org.tidepool.sdk

import org.tidepool.sdk.model.auth.Realm
import org.tidepool.sdk.model.auth.TokenRequest
import org.tidepool.sdk.model.auth.TokenResponse
import org.tidepool.sdk.service.AuthenticationService
import java.time.Instant

class TidepoolAuth(
    private val authentication: AuthenticationService
) : TokenProvider {
    
    private var token: TokenResponse? = null
    private var realm: Realm? = null
    private var clientId: String? = null
    
    suspend fun logIn(
        realm: Realm,
        clientId: String,
        username: String,
        password: String,
        clientSecret: String? = null,
    ): Result<TokenResponse> {
        this.realm = realm
        this.clientId = clientId
        return authentication.obtainTokenWithPassword(
            realm = realm,
            clientId = clientId,
            username = username,
            password = password,
            clientSecret = clientSecret,
        )
    }
    
    override suspend fun getToken(): String = token
        ?.takeIf { it.expiresAt > Instant.now() }
        ?.accessToken
        ?: authentication.obtainToken(
            realm = realm!!,
            tokenRequest = TokenRequest.withRefreshToken(
                clientId = clientId!!,
                initializer = {},
            ),
        ).getOrThrow()
            .also { token = it }
            .accessToken
}