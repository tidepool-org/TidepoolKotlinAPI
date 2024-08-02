package org.tidepool.sdk.deserialization

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer

inline fun <reified E, reified B> GsonBuilder.registerNewDeserializer(jsonTypeName: String = "type") where E : Enum<E>, E: ResultType<B> {
	val deserializer = JsonDeserializer<B> { json, _, context ->
		val enum = enumValueOf<E>(json.asJsonObject.get(jsonTypeName).asString)
		// Avoid recursion
		if (enum.subclassType == B::class) {
			throw defaultDeserializationException
		}
		context.deserialize(json, enum.subclassType.java)
	}
	this.registerTypeAdapter(B::class.java, deserializer)
}

val defaultDeserializationException : IllegalStateException by lazy {
	IllegalStateException("Cannot deserialize to base class")
}