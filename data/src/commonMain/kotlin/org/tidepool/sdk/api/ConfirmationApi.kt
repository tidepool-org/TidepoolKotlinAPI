package org.tidepool.sdk.api

import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.confirmation.AcceptanceDto
import org.tidepool.sdk.dto.confirmation.ConfirmationDto
import org.tidepool.sdk.dto.confirmation.ConfirmationLookupDto
import org.tidepool.sdk.dto.confirmation.ConfirmationUpsertDto
import org.tidepool.sdk.dto.confirmation.InvitationDto
import org.tidepool.sdk.dto.confirmation.PasswordChangeDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface ConfirmationApi {

    // Account Signup Confirmations

    @POST("confirm/send/signup/{userId}")
    suspend fun sendAccountSignupConfirmation(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body requestBody: ConfirmationUpsertDto
    )

    @POST("confirm/resend/signup/{email}")
    suspend fun resendAccountSignup(
        @Path("email") email: String
    )

    @PUT("confirm/accept/signup/{key}")
    suspend fun confirmAccountSignup(
        @Path("key") key: String,
        @Body requestBody: AcceptanceDto
    )

    @PUT("confirm/dismiss/signup/{userId}")
    suspend fun dismissAccountSignup(
        @Path("userId") userId: String,
        @Body requestBody: ConfirmationLookupDto
    )

    @GET("confirm/signup/{userId}")
    suspend fun getAccountSignupConfirmation(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): ConfirmationDto

    @POST("confirm/signup/{userId}")
    suspend fun upsertAccountSignupConfirmation(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body requestBody: ConfirmationUpsertDto
    ): ConfirmationDto

    @PUT("confirm/signup/{userId}")
    suspend fun cancelAccountSignupConfirmation(
        @Path("userId") userId: String,
        @Body requestBody: ConfirmationLookupDto
    )

    // Password Reset

    @POST("confirm/forgot/{email}")
    suspend fun sendPasswordReset(
        @Path("email") email: String,
        @Body requestBody: JsonObject = JsonObject(content = emptyMap()),
    )

    @PUT("confirm/accept/forgot")
    suspend fun acceptPasswordChange(
        @Body requestBody: PasswordChangeDto
    )

    // Care Team Invitations

    @POST("confirm/send/invite/{userId}")
    suspend fun sendCareTeamInvite(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body requestBody: InvitationDto
    ): ConfirmationDto

    @GET("confirm/invite/{userId}")
    suspend fun getPendingCareTeamInvitations(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): List<ConfirmationDto>
    
    @GET("confirm/invitations/{userId}")
    suspend fun getReceivedInvitations( // TODO: this should return empty list, not error
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String
    ): List<ConfirmationDto>
    
    @PUT("confirm/accept/invite/{userId}/{invitedBy}")
    suspend fun acceptCareTeamInvite(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("invitedBy") invitedBy: String,
        @Body requestBody: ConfirmationLookupDto
    )
    
    @PUT("confirm/dismiss/invite/{userId}/{invitedBy}")
    suspend fun dismissInvite(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("invitedBy") invitedBy: String,
        @Body requestBody: ConfirmationLookupDto
    )

    @PUT("confirm/{userId}/invited/{invitedBy}")
    suspend fun cancelInvite(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Path("invitedBy") invitedBy: String
    )
}
