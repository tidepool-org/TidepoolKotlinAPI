package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import org.tidepool.sdk.api.UserApi
import org.tidepool.sdk.di.provideUserApi
import org.tidepool.sdk.dto.user.toDomain
import org.tidepool.sdk.model.metadata.users.User
import org.tidepool.sdk.runCatchingNetworkExceptions

class UserRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
) : UserRepository {

    private val userApi: UserApi
        get() = provideUserApi(environmentRepository.getKtorfit(httpClient))

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