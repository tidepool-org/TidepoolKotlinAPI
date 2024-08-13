package org.tidepool.sdk.requests

import org.tidepool.sdk.model.confirmations.Confirmation
import org.tidepool.sdk.model.confirmations.ConfirmationLookup
import retrofit2.Response
import retrofit2.http.*

interface Confirmations {
    
    @GET("/confirm/invite/{userId}")
    suspend fun getPendingCareTeamInvitations(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): Response<Array<Confirmation>>
    
    @GET("/confirm/invitations/{userId}")
    suspend fun getReceivedInvitations(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): Response<Array<Confirmation>>
    
    @PUT("/confirm/accept/invite/{userId}/{invitedBy}")
    suspend fun acceptCareTeamInvite(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("invitedBy") invitedBy: String,
        @Body requestBody: ConfirmationLookup
    )
    
    @PUT("/confirm/dismiss/invite/{userId}/{invitedBy}")
    suspend fun dismissInvite(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("invitedBy") invitedBy: String,
        @Body requestBody: ConfirmationLookup
    )
}

suspend fun Confirmations.pendingCareTeamInvitations(
    sessionToken: String,
    userId: String
): Array<Confirmation> {
    return getPendingCareTeamInvitations(sessionToken, userId).body() ?: arrayOf()
}

suspend fun Confirmations.receivedInvitations(
    sessionToken: String,
    userId: String
): Array<Confirmation> {
    return getReceivedInvitations(sessionToken, userId).body() ?: arrayOf()
}

private suspend fun Confirmation.perform(
    sessionToken: String,
    userId: String,
    func: suspend (String, String, String, ConfirmationLookup) -> Unit
) {
    func(sessionToken, userId, creatorId, ConfirmationLookup(key))
}

suspend fun Confirmations.accept(
    sessionToken: String,
    userId: String,
    confirmation: Confirmation
) {
    confirmation.perform(sessionToken, userId, this::acceptCareTeamInvite)
}

suspend fun Confirmations.dismiss(
    sessionToken: String,
    userId: String,
    confirmation: Confirmation
) {
    confirmation.perform(sessionToken, userId, this::dismissInvite)
}