package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.confirmation.ConfirmationDto
import org.tidepool.sdk.dto.metadata.users.PermissionsDto

interface ConfirmationRepository {

    // Account Signup Confirmations

    suspend fun sendAccountSignupConfirmation(
        sessionToken: String,
        userId: String,
        clinicId: String? = null,
        invitedBy: String? = null,
    ): Result<Unit>

    suspend fun resendAccountSignup(
        email: String,
    ): Result<Unit>

    suspend fun confirmAccountSignup(
        key: String,
        password: String,
        birthday: String,
    ): Result<Unit>

    suspend fun dismissAccountSignup(
        userId: String,
        confirmationKey: String,
    ): Result<Unit>

    suspend fun getAccountSignupConfirmation(
        sessionToken: String,
        userId: String,
    ): Result<ConfirmationDto>

    suspend fun upsertAccountSignupConfirmation(
        sessionToken: String,
        userId: String,
        clinicId: String? = null,
        invitedBy: String? = null,
    ): Result<ConfirmationDto>

    suspend fun cancelAccountSignupConfirmation(
        userId: String,
        confirmationKey: String,
    ): Result<Unit>

    // Password Reset

    suspend fun sendPasswordReset(
        email: String,
    ): Result<Unit>

    suspend fun acceptPasswordChange(
        key: String,
        password: String,
        email: String,
    ): Result<Unit>

    // Care Team Invitations

    suspend fun sendCareTeamInvite(
        sessionToken: String,
        userId: String,
        email: String,
        permissions: PermissionsDto,
        nickname: String? = null,
    ): Result<ConfirmationDto>

    suspend fun getPendingCareTeamInvitations(
        sessionToken: String,
        userId: String,
    ): Result<List<ConfirmationDto>>

    suspend fun getReceivedInvitations(
        sessionToken: String,
        userId: String,
    ): Result<List<ConfirmationDto>>

    suspend fun acceptConfirmation(
        sessionToken: String,
        userId: String,
        confirmationKey: String,
        creatorId: String,
    ): Result<Unit>
    
    suspend fun dismissConfirmation(
        sessionToken: String,
        userId: String,
        confirmationKey: String,
        creatorId: String,
    ): Result<Unit>

    suspend fun cancelInvite(
        sessionToken: String,
        userId: String,
        invitedBy: String,
    ): Result<Unit>
}