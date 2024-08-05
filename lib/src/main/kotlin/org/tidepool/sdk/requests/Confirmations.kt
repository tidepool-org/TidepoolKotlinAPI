package org.tidepool.sdk.requests

import org.tidepool.sdk.model.confirmations.Confirmation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface Confirmations {
    @GET("/confirm/invite/{userId}")
    suspend fun getPendingCareTeamInvitations(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ) : Response<Array<Confirmation>>

	@GET("/confirm/invitations/{userId}")
	suspend fun getReceivedInvitations(
		@Header("X-Tidepool-Session-Token") sessionToken: String,
		@Path("userId") userId: String
	) : Response<Array<Confirmation>>
}

suspend fun Confirmations.pendingCareTeamInvitations(
    sessionToken: String,
    userId: String
): Array<Confirmation> {
    return  getPendingCareTeamInvitations(sessionToken, userId).body() ?: arrayOf()
}

suspend fun Confirmations.receivedInvitations(
    sessionToken: String,
    userId: String
): Array<Confirmation> {
    return getReceivedInvitations(sessionToken, userId).body() ?: arrayOf()
}