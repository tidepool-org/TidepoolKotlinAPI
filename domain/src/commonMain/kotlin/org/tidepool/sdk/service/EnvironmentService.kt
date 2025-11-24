package org.tidepool.sdk.service

import org.tidepool.sdk.Environment
import org.tidepool.sdk.repository.EnvironmentRepository
import org.tidepool.sdk.sortList

class EnvironmentService internal constructor(
    private val environmentRepository: EnvironmentRepository,
) {
    suspend fun getEnvironmentOptions(): Result<List<Environment>> = environmentRepository
        .getEnvironmentOptions()
        .sortList { it.url }

    fun setEnvironment(environment: Environment) {
        environmentRepository.setEnvironment(environment)
    }

    fun getEnvironment(): Environment? = environmentRepository.getEnvironment()
}