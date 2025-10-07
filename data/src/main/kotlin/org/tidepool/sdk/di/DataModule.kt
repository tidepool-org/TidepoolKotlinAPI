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
import org.tidepool.sdk.api.*
import org.tidepool.sdk.deserialization.InstantSerializer
import org.tidepool.sdk.dto.data.*
import org.tidepool.sdk.repository.*
import org.tidepool.sdk.repository.impl.*
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
    
    single { get<Retrofit>(qualifier = named("auth")).create(AuthenticationApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(AuthorizationApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(ConfirmationApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(DataApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(MetadataApi::class.java) }
    single { get<Retrofit>(qualifier = named("main")).create(UserApi::class.java) }
    
    singleOf(::AuthenticationRepositoryImpl) bind AuthenticationRepository::class
    singleOf(::AuthorizationRepositoryImpl) bind AuthorizationRepository::class
    singleOf(::ConfirmationRepositoryImpl) bind ConfirmationRepository::class
    singleOf(::DataRepositoryImpl) bind DataRepository::class
    singleOf(::MetadataRepositoryImpl) bind MetadataRepository::class
    singleOf(::UserRepositoryImpl) bind UserRepository::class
}