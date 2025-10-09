package org.tidepool.sdk

import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.tidepool.sdk.di.dataModule
import org.tidepool.sdk.di.domainModule
import org.tidepool.sdk.service.ConfirmationService
import org.tidepool.sdk.service.DataService
import org.tidepool.sdk.service.MetadataService
import org.tidepool.sdk.service.SummaryService
import org.tidepool.sdk.service.UserService
import java.time.Instant

interface TokenProvider {
    suspend fun getToken(): String
}

class TidepoolSDK(
    environment: Environment,
    private val tokenProvider: TokenProvider,
) {
    
    // Internal DI container - not exposed
    private val koin: Koin by lazy {
        startKoin {
            modules(
                // Environment module
                module {
                    single<EnvironmentInternal> { environment.toInternal() }
                    single<TokenProvider> { tokenProvider }
                },
                domainModule,
                dataModule,
            )
        }.koin
    }
    
    val confirmations: ConfirmationService by lazy { koin.get() }
    val data: DataService by lazy { koin.get() }
    val metadata: MetadataService by lazy { koin.get() }
    val summaries: SummaryService by lazy { koin.get() }
    val users: UserService by lazy { koin.get() }
    
    public fun shutdown() {
        stopKoin()
    }
}
