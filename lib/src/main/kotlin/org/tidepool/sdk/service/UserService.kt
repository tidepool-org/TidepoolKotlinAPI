package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.dto.user.UserDto
import org.tidepool.sdk.model.metadata.users.User
import org.tidepool.sdk.model.metadata.users.toDomain
import org.tidepool.sdk.repository.UserRepository

class UserService internal constructor(
    private val repository: UserRepository,
    private val tokenProvider: TokenProvider,
) {
    
    suspend fun getCurrentUser(): Result<User> = repository.getCurrentUser(
        sessionToken = tokenProvider.getToken(),
    ).map(UserDto::toDomain)
    
    suspend fun getUser(
        userId: String,
    ): Result<User> = repository.getUser(
        sessionToken = tokenProvider.getToken(),
        userId = userId,
    ).map(UserDto::toDomain)
}