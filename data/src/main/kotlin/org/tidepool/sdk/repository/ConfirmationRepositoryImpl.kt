package org.tidepool.sdk.repository

import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.api.ConfirmationApi
import org.tidepool.sdk.dto.confirmation.AcceptanceDto
import org.tidepool.sdk.dto.confirmation.ConfirmationDto
import org.tidepool.sdk.dto.confirmation.ConfirmationLookupDto
import org.tidepool.sdk.dto.confirmation.toDomain
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.confirmation.Confirmation
import org.tidepool.sdk.model.confirmation.ConfirmationLookup
import org.tidepool.sdk.dto.confirmation.ConfirmationUpsertDto
import org.tidepool.sdk.dto.confirmation.InvitationDto
import org.tidepool.sdk.dto.confirmation.PasswordChangeDto
import org.tidepool.sdk.dto.metadata.users.PermissionDto
import org.tidepool.sdk.dto.metadata.users.PermissionsDto
import org.tidepool.sdk.model.metadata.users.Permission
import org.tidepool.sdk.repository.ConfirmationRepository
import org.tidepool.sdk.runCatchingNetworkExceptions
import org.tidepool.sdk.runWithRetry
import java.security.Permissions

class ConfirmationRepositoryImpl(
    private val confirmationApi: ConfirmationApi,
) : ConfirmationRepository {
    
    // Account Signup Confirmations
    
    override suspend fun sendAccountSignupConfirmation(
        sessionToken: String,
        userId: String,
        clinicId: String?,
        invitedBy: String?,
    ): Result<Unit> = runCatchingNetworkExceptions {
        confirmationApi.sendAccountSignupConfirmation(
            sessionToken = sessionToken,
            userId = userId,
            requestBody = ConfirmationUpsertDto(
                clinicId = clinicId,
                invitedBy = invitedBy,
            ),
        )
    }
    
    override suspend fun resendAccountSignup(
        email: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        confirmationApi.resendAccountSignup(email = email)
    }
    
    override suspend fun confirmAccountSignup(
        key: String,
        password: String,
        birthday: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        confirmationApi.confirmAccountSignup(
            key = key,
            requestBody = AcceptanceDto(
                password = password,
                birthday = birthday,
            ),
        )
    }
    
    override suspend fun dismissAccountSignup(
        userId: String,
        confirmationKey: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        confirmationApi.dismissAccountSignup(
            userId = userId,
            requestBody = ConfirmationLookupDto(
                key = confirmationKey,
            ),
        )
    }
    
    override suspend fun getAccountSignupConfirmation(
        sessionToken: String,
        userId: String,
    ): Result<Confirmation> = runCatchingNetworkExceptions {
        confirmationApi.getAccountSignupConfirmation(
            sessionToken = sessionToken,
            userId = userId,
        )
    }.map { it.toDomain() }
    
    override suspend fun upsertAccountSignupConfirmation(
        sessionToken: String,
        userId: String,
        clinicId: String?,
        invitedBy: String?,
    ): Result<Confirmation> = runCatchingNetworkExceptions {
        confirmationApi.upsertAccountSignupConfirmation(
            sessionToken = sessionToken,
            userId = userId,
            requestBody = ConfirmationUpsertDto(
                clinicId = clinicId,
                invitedBy = invitedBy,
            ),
        )
    }.map { it.toDomain() }
    
    override suspend fun cancelAccountSignupConfirmation(
        userId: String,
        confirmationKey: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        confirmationApi.cancelAccountSignupConfirmation(
            userId = userId,
            requestBody = ConfirmationLookupDto(
                key = confirmationKey,
            ),
        )
    }
    
    // Password Reset
    
    override suspend fun sendPasswordReset(
        email: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        confirmationApi.sendPasswordReset(email = email)
    }
    
    override suspend fun acceptPasswordChange(
        key: String,
        password: String,
        email: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        confirmationApi.acceptPasswordChange(
            requestBody = PasswordChangeDto(
                key = key,
                password = password,
                email = email,
            ),
        )
    }
    
    // Care Team Invitations
    
    override suspend fun sendCareTeamInvite(
        sessionToken: String,
        userId: String,
        email: String,
        permissions: Set<Permission>,
        nickname: String?,
    ): Result<Confirmation> = runCatchingNetworkExceptions {
        confirmationApi.sendCareTeamInvite(
            sessionToken = sessionToken,
            userId = userId,
            requestBody = InvitationDto(
                email = email,
                permissions = PermissionsDto(permissions = permissions),
                nickname = nickname,
            ),
        )
    }.map { it.toDomain() }
    
    override suspend fun getPendingCareTeamInvitations(
        sessionToken: String,
        userId: String,
    ): Result<List<Confirmation>> = runCatchingNetworkExceptions {
        confirmationApi.getPendingCareTeamInvitations(
            sessionToken = sessionToken,
            userId = userId,
        )
    }.mapList { it.toDomain() }
    
    override suspend fun getReceivedInvitations(
        sessionToken: String,
        userId: String,
    ): Result<List<Confirmation>> = runWithRetry {
        confirmationApi.getReceivedInvitations(
            sessionToken = sessionToken,
            userId = userId,
        )
    }.mapList { it.toDomain() }
    
    override suspend fun acceptConfirmation(
        sessionToken: String,
        userId: String,
        confirmationKey: String,
        creatorId: String,
    ) = runCatchingNetworkExceptions {
        confirmationApi.acceptCareTeamInvite(
            sessionToken = sessionToken,
            userId = userId,
            invitedBy = creatorId,
            requestBody = ConfirmationLookupDto(
                key = confirmationKey,
            ),
        )
    }
    
    override suspend fun dismissConfirmation(
        sessionToken: String,
        userId: String,
        confirmationKey: String,
        creatorId: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        confirmationApi.dismissInvite(
            sessionToken = sessionToken,
            userId = userId,
            invitedBy = creatorId,
            requestBody = ConfirmationLookupDto(
                key = confirmationKey,
            ),
        )
    }
    
    override suspend fun cancelInvite(
        sessionToken: String,
        userId: String,
        invitedBy: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        confirmationApi.cancelInvite(
            sessionToken = sessionToken,
            userId = userId,
            invitedBy = invitedBy,
        )
    }
}