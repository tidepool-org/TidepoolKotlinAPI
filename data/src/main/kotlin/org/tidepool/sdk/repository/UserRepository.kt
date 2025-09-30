package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.user.UserDto

interface UserRepository {

    suspend fun getCurrentUser(sessionToken: String): Result<UserDto>

    suspend fun getUser(
        userId: String,
        sessionToken: String,
    ): Result<UserDto>
}