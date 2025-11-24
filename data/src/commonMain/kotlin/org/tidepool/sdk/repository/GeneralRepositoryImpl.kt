package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import org.tidepool.sdk.api.GeneralApi
import org.tidepool.sdk.di.provideGeneralApi
import org.tidepool.sdk.dto.general.MinimumClientVersionsDto
import org.tidepool.sdk.dto.general.toDomain
import org.tidepool.sdk.model.general.MinimumClientVersions
import org.tidepool.sdk.runCatchingNetworkExceptions

class GeneralRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
) : GeneralRepository {

    private val generalApi: GeneralApi
        get() = provideGeneralApi(environmentRepository.getKtorfit(httpClient))

    override suspend fun getMinimumClientVersions() = runCatchingNetworkExceptions {
        generalApi.getMinimumClientVersions()
    }.map { it.toDomain() }
}