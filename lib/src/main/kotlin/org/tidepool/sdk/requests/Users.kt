package org.tidepool.sdk.requests

import org.tidepool.sdk.model.metadata.users.User
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface Users {
    
    @GET("/auth/user")
    suspend fun getCurrentUserInfo(
        @Header("X-Tidepool-Session-Token") sessionToken: String
    ): User
    
    @GET("/auth/user/{userId}")
    suspend fun getUserInfo(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): User
}