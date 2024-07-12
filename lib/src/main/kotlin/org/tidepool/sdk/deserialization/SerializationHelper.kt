package org.tidepool.sdk.deserialization

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import java.lang.reflect.Type

inline fun <reified E, reified B> GsonBuilder.registerNewDeserializer(jsonTypeName: String = "type") where E : Enum<E>, E: ResultType<B> {
	val deserializer = object : JsonDeserializer<B> {
		override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): B {
			val enum = enumValueOf<E>(json.asJsonObject.get(jsonTypeName).asString)
			// Avoid recursion
			if (enum.subclassType == B::class) {
				throw defaultDeserializationException
			}
			return context.deserialize(json, enum.subclassType.java)
		}
	}
	this.registerTypeAdapter(B::class.java, deserializer)
}

val defaultDeserializationException : IllegalStateException by lazy {
	IllegalStateException("Cannot deserialize to base class")
}