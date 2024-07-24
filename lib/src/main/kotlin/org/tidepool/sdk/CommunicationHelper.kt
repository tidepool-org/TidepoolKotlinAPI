package org.tidepool.sdk

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.tidepool.sdk.auth.Auth
import org.tidepool.sdk.deserialization.ResultType
import org.tidepool.sdk.model.data.BaseData
import java.net.URL
import java.lang.reflect.Type
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlin.reflect.typeOf
import org.tidepool.sdk.deserialization.registerNewDeserializer
import org.tidepool.sdk.data.Data
import org.tidepool.sdk.deserialization.InstantSerializer
import org.tidepool.sdk.requests.Users
import java.time.Instant

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

		internal val gsonBuilder: GsonBuilder
			get() = GsonBuilder().apply {
				registerNewDeserializer<BaseData.DataType, BaseData>()
				registerTypeAdapter(Instant::class.java, InstantSerializer())
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
}