package org.tidepool.sdk.api

import org.tidepool.sdk.dto.user.UserDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserApi {

    @GET("/auth/user")
    suspend fun getCurrentUserInfo(
        @Header("X-Tidepool-Session-Token") sessionToken: String
    ): UserDto
    
    @GET("/auth/user/{userId}")
    suspend fun getUserInfo(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): UserDto
}