package org.tidepool.sdk.api

import org.tidepool.sdk.dto.confirmation.ConfirmationDto
import org.tidepool.sdk.dto.confirmation.ConfirmationLookupDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ConfirmationApi {
    
    @GET("/confirm/invite/{userId}")
    suspend fun getPendingCareTeamInvitations(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): List<ConfirmationDto>
    
    @GET("/confirm/invitations/{userId}")
    suspend fun getReceivedInvitations( // TODO: this should return empty list, not error
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): List<ConfirmationDto>
    
    @PUT("/confirm/accept/invite/{userId}/{invitedBy}")
    suspend fun acceptCareTeamInvite(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("invitedBy") invitedBy: String,
        @Body requestBody: ConfirmationLookupDto
    )
    
    @PUT("/confirm/dismiss/invite/{userId}/{invitedBy}")
    suspend fun dismissInvite(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("invitedBy") invitedBy: String,
        @Body requestBody: ConfirmationLookupDto
    )
}
