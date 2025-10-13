package org.tidepool.sdk.repository

import org.tidepool.sdk.api.UserApi
import org.tidepool.sdk.dto.user.UserDto
import org.tidepool.sdk.dto.user.toDomain
import org.tidepool.sdk.model.metadata.users.User
import org.tidepool.sdk.runCatchingNetworkExceptions

class UserRepositoryImpl(
    private val userApi: UserApi,
) : UserRepository {
    
    override suspend fun getCurrentUser(sessionToken: String): Result<User> =
        runCatchingNetworkExceptions {
            userApi.getCurrentUserInfo(sessionToken)
        }.map { it.toDomain() }
    
    
    override suspend fun getUser(
        userId: String,
        sessionToken: String,
    ): Result<User> = runCatchingNetworkExceptions {
        userApi.getUserInfo(
            sessionToken = sessionToken,
            userId = userId,
        )
    }.map { it.toDomain() }
}