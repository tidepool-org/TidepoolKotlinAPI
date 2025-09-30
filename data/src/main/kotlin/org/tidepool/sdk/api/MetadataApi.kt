package org.tidepool.sdk.api

import org.tidepool.sdk.dto.metadata.ProfileDto
import org.tidepool.sdk.dto.metadata.users.TrustUserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MetadataApi {
    
    @GET("/metadata/users/{userId}/users")
    suspend fun getTrustUsers(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): List<TrustUserDto>
    
    @GET("/metadata/{userId}/profile")
    suspend fun getUserProfile(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): ProfileDto
}