package org.tidepool.sdk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.minidns.hla.ResolverApi

class DnsResolverImpl(
    private val timeoutSeconds: Int = 10,
) : DnsResolver {
    override suspend fun resolveSrv(): Result<List<SrvRecord>> =
        withContext(Dispatchers.IO) {
            try {
                // Apply timeout similar to Swift implementation
                val result = withTimeoutOrNull(timeoutSeconds * 1000L) {
                    performLookup()
                }

                result ?: Result.failure(
                    Exception("DNS lookup timed out after $timeoutSeconds seconds")
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private fun performLookup(): Result<List<SrvRecord>> = runCatching {
        val result = ResolverApi.INSTANCE.resolveSrv(hostname)
        result.throwIfErrorResponse()

        result.sortedSrvResolvedAddresses
            .map {
                SrvRecord(
                    target = it.srv.target.toString(),
                    port = it.srv.port,
                    priority = it.srv.priority,
                    weight = it.srv.weight,
                )
            }
    }
}
