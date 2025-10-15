package org.tidepool.sdk.repository

import org.tidepool.sdk.api.ConfirmationApi
import org.tidepool.sdk.dto.confirmation.ConfirmationLookupDto
import org.tidepool.sdk.dto.confirmation.toDomain
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.confirmation.Confirmation
import org.tidepool.sdk.model.confirmation.ConfirmationLookup
import org.tidepool.sdk.runCatchingNetworkExceptions
import org.tidepool.sdk.runWithRetry

class ConfirmationRepositoryImpl(
    private val confirmationApi: ConfirmationApi,
) : ConfirmationRepository {
    
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
}