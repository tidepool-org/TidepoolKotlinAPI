package org.tidepool.sdk

interface TokenProvider {
    suspend fun getToken(): Result<String>
}