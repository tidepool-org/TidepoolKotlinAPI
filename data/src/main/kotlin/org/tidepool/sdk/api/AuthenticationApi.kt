package org.tidepool.sdk.api

import org.tidepool.sdk.dto.auth.TokenRequestDto
import org.tidepool.sdk.dto.auth.TokenResponseDto
import org.tidepool.sdk.dto.auth.RealmDto
import kotlinx.serialization.SerialName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthenticationApi {

    @POST("/realms/{realm}/protocol/openid-connect/token")
    suspend fun obtainToken(
        @Path("realm") realm: String,
        @Body grantType: TokenRequestDto,
    ): TokenResponseDto
    
    @GET("/realms/{realm}/protocol/openid-connect/auth")
    suspend fun authorize(
        @Path("realm") realm: String,
        @Query("client_id") clientId: String,
        @Query("scope") scope: String,
        @Query("response_type") responseType: String = "code",
        @Query("redirect_uri") redirectUri: String,
        @Query("login_hint") loginHint: String? = null,
        @Query("kc_idp_hint") kcIdpHint: String? = null,
        @Query("prompt") prompt: String? = null,
    ): String
    
    enum class PromptType {
        @SerialName("none")
        None,

        @SerialName("login")
        Login
    }

    enum class ScopeType {
        @SerialName("openid")
        Openid,

        @SerialName("email")
        Email
    }
}