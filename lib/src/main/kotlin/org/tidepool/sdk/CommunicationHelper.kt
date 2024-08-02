package org.tidepool.sdk

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.tidepool.sdk.auth.Auth
import org.tidepool.sdk.requests.Data
import org.tidepool.sdk.deserialization.InstantSerializer
import org.tidepool.sdk.deserialization.TrustUserDeserializer
import org.tidepool.sdk.deserialization.registerNewDeserializer
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.metadata.users.TrustUser
import org.tidepool.sdk.requests.Confirmations
import org.tidepool.sdk.requests.Metadata
import org.tidepool.sdk.requests.Users
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import kotlin.reflect.KClass

public class CommunicationHelper(private val environment: Environment) {
	private val retrofit: Retrofit by lazy {
		Retrofit.Builder()
			.baseUrl(environment.url)
			.addConverterFactory(GsonConverterFactory.create(gsonConfig))
			.build()
	}

	private val authRetrofit: Retrofit by lazy {
		Retrofit.Builder()
			.baseUrl(environment.auth.url)
			.addConverterFactory(GsonConverterFactory.create(gsonConfig))
			.build()
	}

	companion object {
		public val gsonConfig: Gson by lazy {
			gsonBuilder.create()
		}

		internal val gsonBuilder: GsonBuilder by lazy {
			GsonBuilder().apply {
				registerNewDeserializer<BaseData.DataType, BaseData>()
				registerTypeAdapter(Instant::class.java, InstantSerializer())
				registerTypeAdapter(TrustUser::class.java, TrustUserDeserializer())
			}
		}
	}

	public val auth: Auth by lazy {
		authRetrofit.create(Auth::class.java)
	}

	public val data: Data by lazy {
		retrofit.create(Data::class.java)
	}

	public val users: Users by lazy {
		retrofit.create(Users::class.java)
	}

	public val metadata: Metadata by lazy {
		retrofit.create(Metadata::class.java)
	}

	public val confirmations: Confirmations by lazy {
		retrofit.create(Confirmations::class.java)
	}
}