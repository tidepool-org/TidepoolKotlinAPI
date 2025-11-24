package org.tidepool.sdk

internal const val hostname = "environments-srv.tidepool.org"
interface DnsResolver {
    suspend fun resolveSrv(): Result<List<SrvRecord>>
}