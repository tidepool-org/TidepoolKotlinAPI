package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.UserApi
import org.tidepool.sdk.dto.user.UserDto
import org.tidepool.sdk.repository.UserRepository
import org.tidepool.sdk.runCatchingNetworkExceptions

class UserRepositoryImpl(
    private val userApi: UserApi,
) : UserRepository {
    
    override suspend fun getCurrentUser(sessionToken: String): Result<UserDto> =
        runCatchingNetworkExceptions {
            userApi.getCurrentUserInfo(sessionToken)
        }
    
    
    override suspend fun getUser(
        userId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        userApi.getUserInfo(
            sessionToken = sessionToken,
            userId = userId,
        )
    }
}