package org.tidepool.sdk.repository

import org.tidepool.sdk.DnsResolver
import org.tidepool.sdk.Environment

private const val KEY_ENVIRONMENT = "KEY_ENVIRONMENT"

class EnvironmentRepositoryImpl(
    private val dnsResolver: DnsResolver,
    private val keyValueStorage: KeyValueStorage,
) : EnvironmentRepository {


    override suspend fun getEnvironmentOptions() = dnsResolver.resolveSrv()
        .map{ list ->
            list
                .sortedBy { it.priority }
                .map { Environment(it.target) }
        }

    override fun setEnvironment(environment: Environment) {
        keyValueStorage.putString(KEY_ENVIRONMENT, environment.url)
    }

    override fun getEnvironment() = keyValueStorage.getString(KEY_ENVIRONMENT)
        ?.let { Environment(url = it) }
}