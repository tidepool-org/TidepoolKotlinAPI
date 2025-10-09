package org.tidepool.sdk.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.tidepool.sdk.EnvironmentInternal
import org.tidepool.sdk.api.AuthApi
import org.tidepool.sdk.api.ConfirmationApi
import org.tidepool.sdk.api.DataApi
import org.tidepool.sdk.api.MetadataApi
import org.tidepool.sdk.api.SummaryApi
import org.tidepool.sdk.api.UserApi
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
import org.tidepool.sdk.repository.AuthRepository
import org.tidepool.sdk.repository.ConfirmationRepository
import org.tidepool.sdk.repository.DataRepository
import org.tidepool.sdk.repository.MetadataRepository
import org.tidepool.sdk.repository.SummaryRepository
import org.tidepool.sdk.repository.UserRepository
import org.tidepool.sdk.repository.impl.AuthRepositoryImpl
import org.tidepool.sdk.repository.impl.ConfirmationRepositoryImpl
import org.tidepool.sdk.repository.impl.DataRepositoryImpl
import org.tidepool.sdk.repository.impl.MetadataRepositoryImpl
import org.tidepool.sdk.repository.impl.SummaryRepositoryImpl
import org.tidepool.sdk.repository.impl.UserRepositoryImpl
import retrofit2.Retrofit
import java.time.Instant

public val dataModule = module {
    
    // Session Manager
    // JSON Configuration
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
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
    
    single {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
            )
            .build()
    }
    
    // TODO: Retrofit configuration will need Environment class or base URL configuration
    // Main API Retrofit instance
    single<Retrofit>(qualifier = named("main")) {
        val environment: EnvironmentInternal = get()
        val json: Json = get()
        
        Retrofit.Builder()
            .baseUrl(environment.url)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(get())
            .build()
    }
    
    // Auth API Retrofit instance
    single<Retrofit>(qualifier = named("auth")) {
        val environment: EnvironmentInternal = get()
        val json: Json = get()
        Retrofit.Builder()
            .baseUrl(environment.auth.url)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    
    single { get<Retrofit>(qualifier = named("auth")).create(AuthApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(ConfirmationApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(DataApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(MetadataApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(SummaryApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(UserApi::class.java) }
    
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::ConfirmationRepositoryImpl) bind ConfirmationRepository::class
    singleOf(::DataRepositoryImpl) bind DataRepository::class
    singleOf(::MetadataRepositoryImpl) bind MetadataRepository::class
    singleOf(::SummaryRepositoryImpl) bind SummaryRepository::class
    singleOf(::UserRepositoryImpl) bind UserRepository::class
}