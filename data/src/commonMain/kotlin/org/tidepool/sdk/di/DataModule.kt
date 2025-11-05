package org.tidepool.sdk.di

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.tidepool.sdk.Environment
import org.tidepool.sdk.EnvironmentInternal
import org.tidepool.sdk.api.AlertApi
import org.tidepool.sdk.api.AuthorizationApi
import org.tidepool.sdk.api.BlobApi
import org.tidepool.sdk.api.ClinicApi
import org.tidepool.sdk.api.ConfirmationApi
import org.tidepool.sdk.api.DataApi
import org.tidepool.sdk.api.GeneralApi
import org.tidepool.sdk.api.MessageApi
import org.tidepool.sdk.api.MetadataApi
import org.tidepool.sdk.api.MetricsApi
import org.tidepool.sdk.api.PrescriptionApi
import org.tidepool.sdk.api.SummaryApi
import org.tidepool.sdk.api.TaskApi
import org.tidepool.sdk.api.UserApi
import org.tidepool.sdk.database.DataDao
import org.tidepool.sdk.database.LoopKitDatabase
import org.tidepool.sdk.deserialization.InstantSerializer
import org.tidepool.sdk.dto.data.BasalAutomatedDataDto
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.BolusDataDto
import org.tidepool.sdk.dto.data.ContinuousGlucoseDataDto
import org.tidepool.sdk.dto.data.DosingDecisionDataDto
import org.tidepool.sdk.dto.data.FoodDataDto
import org.tidepool.sdk.dto.data.InsulinDataDto
import org.tidepool.sdk.dto.summary.SummaryDto
import org.tidepool.sdk.dto.summary.CgmSummaryDto
import org.tidepool.sdk.dto.summary.BgmSummaryDto
import org.tidepool.sdk.dto.summary.ContinuousSummaryDto
import org.tidepool.sdk.repository.AlertRepository
import org.tidepool.sdk.repository.AuthorizationRepository
import org.tidepool.sdk.repository.BlobRepository
import org.tidepool.sdk.repository.ClinicRepository
import org.tidepool.sdk.repository.ConfirmationRepository
import org.tidepool.sdk.repository.DataRepository
import org.tidepool.sdk.repository.ExportRepository
import org.tidepool.sdk.repository.GeneralRepository
import org.tidepool.sdk.repository.MessageRepository
import org.tidepool.sdk.repository.MetadataRepository
import org.tidepool.sdk.repository.MetricsRepository
import org.tidepool.sdk.repository.PrescriptionRepository
import org.tidepool.sdk.repository.SummaryRepository
import org.tidepool.sdk.repository.TaskRepository
import org.tidepool.sdk.repository.UserRepository
import org.tidepool.sdk.repository.AlertRepositoryImpl
import org.tidepool.sdk.repository.BlobRepositoryImpl
import org.tidepool.sdk.repository.ClinicRepositoryImpl
import org.tidepool.sdk.repository.ConfirmationRepositoryImpl
import org.tidepool.sdk.repository.DataRepositoryImpl
import org.tidepool.sdk.repository.ExportRepositoryImpl
import org.tidepool.sdk.repository.GeneralRepositoryImpl
import org.tidepool.sdk.repository.MessageRepositoryImpl
import org.tidepool.sdk.repository.MetadataRepositoryImpl
import org.tidepool.sdk.repository.MetricsRepositoryImpl
import org.tidepool.sdk.repository.PrescriptionRepositoryImpl
import org.tidepool.sdk.repository.SummaryRepositoryImpl
import org.tidepool.sdk.repository.TaskRepositoryImpl
import org.tidepool.sdk.repository.UserRepositoryImpl
import org.tidepool.sdk.repository.AuthorizationRepositoryImpl
import org.tidepool.sdk.toInternal
import java.time.Instant
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import io.ktor.http.ContentType
import io.ktor.http.contentType
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger

expect val platformDataModule: Module

expect fun provideAlertApi(ktorfit: Ktorfit): AlertApi
expect fun provideAuthorizationApi(ktorfit: Ktorfit): AuthorizationApi
expect fun provideBlobApi(ktorfit: Ktorfit): BlobApi
expect fun provideClinicApi(ktorfit: Ktorfit): ClinicApi
expect fun provideConfirmationApi(ktorfit: Ktorfit): ConfirmationApi
expect fun provideDataApi(ktorfit: Ktorfit): DataApi
//expect fun provideExportApi(ktorfit: Ktorfit): ExportApi
expect fun provideGeneralApi(ktorfit: Ktorfit): GeneralApi
expect fun provideMessageApi(ktorfit: Ktorfit): MessageApi
expect fun provideMetadataApi(ktorfit: Ktorfit): MetadataApi
expect fun provideMetricsApi(ktorfit: Ktorfit): MetricsApi
expect fun providePrescriptionApi(ktorfit: Ktorfit): PrescriptionApi
expect fun provideSummaryApi(ktorfit: Ktorfit): SummaryApi
expect fun provideTaskApi(ktorfit: Ktorfit): TaskApi
expect fun provideUserApi(ktorfit: Ktorfit): UserApi

public val dataModule = module {
    
    // JSON Configuration
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
            explicitNulls = false
            classDiscriminator =
                "__type"  // Use different discriminator to avoid conflict with 'type' property
            serializersModule = SerializersModule {
                contextual(Instant::class, InstantSerializer)
                // Configure BaseData polymorphism  
                polymorphic(BaseDataDto::class) {
                    subclass(BasalAutomatedDataDto::class)
                    subclass(BolusDataDto::class)
                    subclass(ContinuousGlucoseDataDto::class)
                    subclass(DosingDecisionDataDto::class)
                    subclass(FoodDataDto::class)
                    subclass(InsulinDataDto::class)
                    // Add other BaseData subclasses as they get implemented
                }
                // Configure SummaryDto polymorphism
                polymorphic(SummaryDto::class) {
                    subclass(CgmSummaryDto::class)
                    subclass(BgmSummaryDto::class)
                    subclass(ContinuousSummaryDto::class)
                }
            }
        }
    }

    // Ktor HttpClient Configuration
    single {
        HttpClient {
            expectSuccess = false // Handle non-2xx responses manually

            install(ContentNegotiation) {
                json(get<Json>())
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 30_000
            }

            install(Logging) {
                logger = Logger.ANDROID
            }

            defaultRequest {
                headers.append("Content-Type", "application/json")
                contentType(ContentType.Application.Json)
            }
        }
    }

    // Main API Ktorfit instance
    single<Ktorfit> {
        val environment: EnvironmentInternal = get<Environment>().toInternal()
        val httpClient: HttpClient = get()

        Ktorfit.Builder()
            .baseUrl(environment.url)
            .httpClient(httpClient)
            .build()
    }

    // API Service Implementations using expect/actual pattern
    single<AlertApi> { provideAlertApi(get<Ktorfit>()) }
    single<AuthorizationApi> { provideAuthorizationApi(get<Ktorfit>()) }
    single<BlobApi> { provideBlobApi(get<Ktorfit>()) }
    single<ClinicApi> { provideClinicApi(get<Ktorfit>()) }
    single<ConfirmationApi> { provideConfirmationApi(get<Ktorfit>()) }
    single<DataApi> { provideDataApi(get<Ktorfit>()) }
//    single<ExportApi> { provideExportApi(get<Ktorfit>()) }
    single<GeneralApi> { provideGeneralApi(get<Ktorfit>()) }
    single<MessageApi> { provideMessageApi(get<Ktorfit>()) }
    single<MetadataApi> { provideMetadataApi(get<Ktorfit>()) }
    single<MetricsApi> { provideMetricsApi(get<Ktorfit>()) }
    single<PrescriptionApi> { providePrescriptionApi(get<Ktorfit>()) }
    single<SummaryApi> { provideSummaryApi(get<Ktorfit>()) }
    single<TaskApi> { provideTaskApi(get<Ktorfit>()) }
    single<UserApi> { provideUserApi(get<Ktorfit>()) }
    
    single<DataDao> { get<LoopKitDatabase>().dataDao() }
    
    singleOf(::AlertRepositoryImpl) bind AlertRepository::class
    singleOf(::AuthorizationRepositoryImpl) bind AuthorizationRepository::class
    singleOf(::BlobRepositoryImpl) bind BlobRepository::class
    singleOf(::ClinicRepositoryImpl) bind ClinicRepository::class
    singleOf(::ConfirmationRepositoryImpl) bind ConfirmationRepository::class
    singleOf(::DataRepositoryImpl) bind DataRepository::class
    singleOf(::ExportRepositoryImpl) bind ExportRepository::class
    singleOf(::GeneralRepositoryImpl) bind GeneralRepository::class
    singleOf(::MessageRepositoryImpl) bind MessageRepository::class
    singleOf(::MetadataRepositoryImpl) bind MetadataRepository::class
    singleOf(::MetricsRepositoryImpl) bind MetricsRepository::class
    singleOf(::PrescriptionRepositoryImpl) bind PrescriptionRepository::class
    singleOf(::SummaryRepositoryImpl) bind SummaryRepository::class
    singleOf(::TaskRepositoryImpl) bind TaskRepository::class
    singleOf(::UserRepositoryImpl) bind UserRepository::class
}