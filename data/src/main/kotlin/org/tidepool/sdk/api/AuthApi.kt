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
import java.net.URI

interface AuthApi {

    @POST("/realms/{realm}/protocol/openid-connect/token")
    suspend fun obtainToken(
        @Path("realm") realm: RealmDto,
        @Body grantType: TokenRequestDto,
    ): TokenResponseDto

    @GET("/realms/{realm}/protocol/openid-connect/auth?response_type=code")
    suspend fun authorize(
        @Path("realm") realm: RealmDto,
        @Query("client_id") clientId: String,
        @Query("scope") scope: Array<ScopeType>,
        @Query("redirect_uri") redirectUri: URI,
        @Query("login_hint") loginHint: String? = null,
        @Query("kc_idp_hint") kcIdpHint: String? = null,
        @Query("prompt") prompt: PromptType? = null,
    )

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