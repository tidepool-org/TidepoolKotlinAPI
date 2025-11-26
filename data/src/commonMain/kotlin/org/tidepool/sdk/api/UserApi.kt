package org.tidepool.sdk.api

import org.tidepool.sdk.dto.user.UserDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Path

interface UserApi {

    @GET("auth/user")
    suspend fun getCurrentUserInfo(
        @Header("X-Tidepool-Session-Token") sessionToken: String
    ): UserDto
    
    @GET("auth/user/{userId}")
    suspend fun getUserInfo(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): UserDto
}