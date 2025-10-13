package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.confirmation.Confirmation
import org.tidepool.sdk.repository.ConfirmationRepository
import org.tidepool.sdk.repository.UserRepository

class ConfirmationService internal constructor(
    private val confirmationRepository: ConfirmationRepository,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) {
    
    suspend fun getReceivedInvitations(): Result<List<Confirmation>> = tokenProvider.getToken()
        .let { token ->
            userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
                confirmationRepository.getReceivedInvitations(
                    sessionToken = token,
                    userId = user.userId,
                )
            }
        }
    
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
}