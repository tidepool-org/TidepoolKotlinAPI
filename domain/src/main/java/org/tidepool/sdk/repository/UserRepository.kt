package org.tidepool.sdk.repository

import org.tidepool.sdk.model.metadata.users.User

interface UserRepository {

    suspend fun getCurrentUser(sessionToken: String): Result<User>

    suspend fun getUser(
        userId: String,
        sessionToken: String,
    ): Result<User>
}