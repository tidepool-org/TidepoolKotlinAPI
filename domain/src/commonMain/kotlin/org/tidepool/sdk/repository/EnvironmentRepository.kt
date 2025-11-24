package org.tidepool.sdk.repository

import org.tidepool.sdk.Environment

interface EnvironmentRepository {
    suspend fun getEnvironmentOptions(): Result<List<Environment>>
    fun setEnvironment(environment: Environment)
    fun getEnvironment(): Environment?
}