package org.tidepool.sdk

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import okhttp3.MediaType.Companion.toMediaType
import org.tidepool.sdk.deserialization.InstantSerializer
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.BolusData
import org.tidepool.sdk.model.data.ContinuousGlucoseData
import org.tidepool.sdk.model.data.DosingDecisionData
import org.tidepool.sdk.model.data.FoodData
import org.tidepool.sdk.model.data.InsulinData
import org.tidepool.sdk.model.metadata.users.TrustUser
import org.tidepool.sdk.model.metadata.users.TrusteeUser
import org.tidepool.sdk.model.metadata.users.TrustorUser
import org.tidepool.sdk.requests.AuthApi
import org.tidepool.sdk.requests.ConfirmationsApi
import org.tidepool.sdk.requests.DataApi
import org.tidepool.sdk.requests.MetadataApi
import org.tidepool.sdk.requests.UsersApi
import retrofit2.Retrofit
import java.time.Instant

public class CommunicationHelper(private val environment: Environment) {
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(environment.url)
            .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    
    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(environment.auth.url)
            .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    
    companion object {
        
        public val jsonConfig: Json by lazy {
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                isLenient = true
                classDiscriminator =
                    "__type"  // Use different discriminator to avoid conflict with 'type' property
                serializersModule = SerializersModule {
                    contextual(Instant::class, InstantSerializer)
                    polymorphic(TrustUser::class) {
                        subclass(TrusteeUser::class)
                        subclass(TrustorUser::class)
                    }
                    // Configure BaseData polymorphism  
                    polymorphic(BaseData::class) {
                        subclass(BasalAutomatedData::class)
                        subclass(BolusData::class)
                        subclass(ContinuousGlucoseData::class)
                        subclass(DosingDecisionData::class)
                        subclass(FoodData::class)
                        subclass(InsulinData::class)
                        // Add other BaseData subclasses as they get implemented
                    }
                }
            }
        }
    }
    
    public val authApi: AuthApi by lazy {
        authRetrofit.create(AuthApi::class.java)
    }
    
    public val dataApi: DataApi by lazy {
        retrofit.create(DataApi::class.java)
    }
    
    public val usersApi: UsersApi by lazy {
        retrofit.create(UsersApi::class.java)
    }
    
    public val metadataApi: MetadataApi by lazy {
        retrofit.create(MetadataApi::class.java)
    }
    
    public val confirmationsApi: ConfirmationsApi by lazy {
        retrofit.create(ConfirmationsApi::class.java)
    }
}