package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.dto.confirmation.ConfirmationDto
import org.tidepool.sdk.dto.metadata.users.PermissionsDto
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.confirmations.Confirmation
import org.tidepool.sdk.model.confirmations.toDomain
import org.tidepool.sdk.repository.ConfirmationRepository
import org.tidepool.sdk.repository.UserRepository

class ConfirmationService internal constructor(
    private val confirmationRepository: ConfirmationRepository,
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
) {
    
    // Account Signup Confirmations
    
    suspend fun sendAccountSignupConfirmation(
        userId: String,
        clinicId: String? = null,
        invitedBy: String? = null,
    ): Result<Unit> = confirmationRepository.sendAccountSignupConfirmation(
        sessionToken = tokenProvider.getToken(),
        userId = userId,
        clinicId = clinicId,
        invitedBy = invitedBy
    )
    
    suspend fun resendAccountSignup(
        email: String,
    ): Result<Unit> = confirmationRepository.resendAccountSignup(email = email)
    
    suspend fun confirmAccountSignup(
        key: String,
        password: String,
        birthday: String,
    ): Result<Unit> = confirmationRepository.confirmAccountSignup(
        key = key,
        password = password,
        birthday = birthday
    )
    
    suspend fun dismissAccountSignup(
        userId: String,
        confirmationKey: String,
    ): Result<Unit> = confirmationRepository.dismissAccountSignup(
        userId = userId,
        confirmationKey = confirmationKey
    )
    
    suspend fun getAccountSignupConfirmation(
        userId: String,
    ): Result<Confirmation> = confirmationRepository.getAccountSignupConfirmation(
        sessionToken = tokenProvider.getToken(),
        userId = userId
    ).map { it.toDomain() }
    
    suspend fun upsertAccountSignupConfirmation(
        userId: String,
        clinicId: String? = null,
        invitedBy: String? = null,
    ): Result<Confirmation> = confirmationRepository.upsertAccountSignupConfirmation(
        sessionToken = tokenProvider.getToken(),
        userId = userId,
        clinicId = clinicId,
        invitedBy = invitedBy
    ).map { it.toDomain() }
    
    suspend fun cancelAccountSignupConfirmation(
        userId: String,
        confirmationKey: String,
    ): Result<Unit> = confirmationRepository.cancelAccountSignupConfirmation(
        userId = userId,
        confirmationKey = confirmationKey
    )
    
    // Password Reset
    
    suspend fun sendPasswordReset(
        email: String,
    ): Result<Unit> = confirmationRepository.sendPasswordReset(email = email)
    
    suspend fun acceptPasswordChange(
        key: String,
        password: String,
        email: String,
    ): Result<Unit> = confirmationRepository.acceptPasswordChange(
        key = key,
        password = password,
        email = email
    )
    
    // Care Team Invitations
    
    suspend fun sendCareTeamInvite(
        userId: String,
        email: String,
        permissions: PermissionsDto,
        nickname: String? = null,
    ): Result<Confirmation> = confirmationRepository.sendCareTeamInvite(
        sessionToken = tokenProvider.getToken(),
        userId = userId,
        email = email,
        permissions = permissions,
        nickname = nickname
    ).map { it.toDomain() }
    
    suspend fun getPendingCareTeamInvitations(
        userId: String,
    ): Result<List<Confirmation>> = confirmationRepository.getPendingCareTeamInvitations(
        sessionToken = tokenProvider.getToken(),
        userId = userId
    ).mapList { dto -> dto.toDomain() }
    
    suspend fun getReceivedInvitations(
    ): Result<List<Confirmation>> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            confirmationRepository.getReceivedInvitations(
                sessionToken = token,
                userId = user.userId,
            )
        }
    }.mapList { dto -> dto.toDomain() }
    
    suspend fun acceptConfirmation(
        confirmationKey: String,
        creatorId: String,
    ): Result<Unit> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            confirmationRepository.acceptConfirmation(
                sessionToken = tokenProvider.getToken(),
                userId = user.userId,
                confirmationKey = confirmationKey,
                creatorId = creatorId,
            )
        }
    }
    
    suspend fun dismissConfirmation(
        confirmationKey: String,
        creatorId: String,
    ): Result<Unit> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            confirmationRepository.dismissConfirmation(
                sessionToken = tokenProvider.getToken(),
                userId = user.userId,
                confirmationKey = confirmationKey,
                creatorId = creatorId,
            )
        }
    }
    
    suspend fun cancelInvite(
        invitedBy: String,
    ): Result<Unit> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            confirmationRepository.cancelInvite(
                sessionToken = tokenProvider.getToken(),
                userId = user.userId,
                invitedBy = invitedBy,
            )
        }
    }
}