package org.tidepool.sdk

import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.tidepool.sdk.di.dataModule
import org.tidepool.sdk.di.domainModule
import org.tidepool.sdk.service.AlertService
import org.tidepool.sdk.service.AuthenticationService
import org.tidepool.sdk.service.AuthorizationService
import org.tidepool.sdk.service.BlobService
import org.tidepool.sdk.service.ClinicService
import org.tidepool.sdk.service.ConfirmationService
import org.tidepool.sdk.service.DataService
import org.tidepool.sdk.service.ExportService
import org.tidepool.sdk.service.GeneralService
import org.tidepool.sdk.service.MetadataService
import org.tidepool.sdk.service.SummaryService
import org.tidepool.sdk.service.UserService

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
    
    val alerts: AlertService by lazy { koin.get() }
    val authentication: AuthenticationService by lazy { koin.get() }
    val authorization: AuthorizationService by lazy { koin.get() }
    val blobs: BlobService by lazy { koin.get() }
    val confirmations: ConfirmationService by lazy { koin.get() }
    val clinics: ClinicService by lazy { koin.get() }
    val data: DataService by lazy { koin.get() }
    val export: ExportService by lazy { koin.get() }
    val general: GeneralService by lazy { koin.get() }
    val metadata: MetadataService by lazy { koin.get() }
    val summaries: SummaryService by lazy { koin.get() }
    val users: UserService by lazy { koin.get() }
    
    public fun shutdown() {
        stopKoin()
    }
}
