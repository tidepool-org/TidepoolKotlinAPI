package org.tidepool.sdk.deserialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.tidepool.sdk.model.metadata.users.TrustUser
import org.tidepool.sdk.model.metadata.users.TrusteeUser
import org.tidepool.sdk.model.metadata.users.TrustorUser
import java.lang.reflect.Type

class TrustUserDeserializer : JsonDeserializer<TrustUser> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): TrustUser {
        val jsonObject = json.asJsonObject
        if (jsonObject.has("trusteePermissions")) {
            return context.deserialize(json, TrusteeUser::class.java)
        } else if (jsonObject.has("trustorPermissions")) {
            return context.deserialize(json, TrustorUser::class.java)
        }
        throw NoSuchElementException("Does not have either trustee or trustor permissions")
    }
}