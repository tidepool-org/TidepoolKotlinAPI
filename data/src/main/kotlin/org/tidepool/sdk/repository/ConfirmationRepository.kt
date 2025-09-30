package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.confirmation.ConfirmationDto

interface ConfirmationRepository {
    
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
}