package org.tidepool.sdk.auth;

import org.tidepool.sdk.model.auth.Realm
import org.tidepool.sdk.requests.TokenRequest
import retrofit2.http.*
import java.net.URI

public interface Auth {
    
    @POST("/realms/{realm}/protocol/openid-connect/token")
    suspend fun obtainToken(
        @Path("realm") realm: Realm,
        @Body grantType: TokenRequest
    ): TokenResponse
    
    @GET("/realms/{realm}/protocol/openid-connect/auth?response_type=code")
    suspend fun authorize(
        @Path("realm") realm: Realm,
        @Query("client_id") clinetId: String,
        @Query("scope") scope: Array<ScopeType>,
        @Query("redirect_uri") redirectUri: URI,
        @Query("login_hint") loginHint: String? = null,
        @Query("kc_idp_hint") kcIdpHint: String? = null,
        @Query("prompt") prompt: PromptType? = null
    )
    
    enum class PromptType {
        none,
        login
    }
    
    enum class ScopeType {
        openid,
        email
    }
}