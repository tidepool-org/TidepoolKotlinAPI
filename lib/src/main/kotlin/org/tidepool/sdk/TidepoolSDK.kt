package org.tidepool.sdk

import org.koin.core.Koin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.tidepool.sdk.di.dataModule
import org.tidepool.sdk.di.domainModule
import org.tidepool.sdk.service.AlertService
import org.tidepool.sdk.service.AuthorizationService
import org.tidepool.sdk.service.BlobService
import org.tidepool.sdk.service.ClinicService
import org.tidepool.sdk.service.ConfirmationService
import org.tidepool.sdk.service.DataService
import org.tidepool.sdk.service.ExportService
import org.tidepool.sdk.service.GeneralService
import org.tidepool.sdk.service.MessageService
import org.tidepool.sdk.service.MetadataService
import org.tidepool.sdk.service.MetricsService
import org.tidepool.sdk.service.PrescriptionService
import org.tidepool.sdk.service.SummaryService
import org.tidepool.sdk.service.TaskService
import org.tidepool.sdk.service.UserService

class TidepoolSDK(
    environment: Environment,
    private val tokenProvider: TokenProvider,
) {
    
    // Internal DI container - not exposed
    
    private val koinApp = koinApplication {
        modules(
            // Environment module
            module {
                single<Environment> { environment }
                single<TokenProvider> { tokenProvider }
            },
            domainModule,
            dataModule,
        )
    }
    private val koin: Koin = koinApp.koin
    
    val alerts: AlertService by lazy { koin.get() }
    val authorization: AuthorizationService by lazy { koin.get() }
    val blobs: BlobService by lazy { koin.get() }
    val confirmations: ConfirmationService by lazy { koin.get() }
    val clinics: ClinicService by lazy { koin.get() }
    val data: DataService by lazy { koin.get() }
    val export: ExportService by lazy { koin.get() }
    val general: GeneralService by lazy { koin.get() }
    val messages: MessageService by lazy { koin.get() }
    val metadata: MetadataService by lazy { koin.get() }
    val metrics: MetricsService by lazy { koin.get() }
    val prescriptions: PrescriptionService by lazy { koin.get() }
    val summaries: SummaryService by lazy { koin.get() }
    val tasks: TaskService by lazy { koin.get() }
    val users: UserService by lazy { koin.get() }
    
    public fun shutdown() {
        koinApp.close()
    }
}
