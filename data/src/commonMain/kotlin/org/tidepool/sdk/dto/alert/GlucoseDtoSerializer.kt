package org.tidepool.sdk.dto.alert

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object GlucoseDtoSerializer : JsonContentPolymorphicSerializer<GlucoseDto>(GlucoseDto::class) {
    
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<GlucoseDto> {
        val units = element.jsonObject["units"]?.jsonPrimitive?.content
            ?: throw SerializationException("Missing 'units' field in glucose object")
        
        return when {
            units.contains("mg/d", ignoreCase = true) -> GlucoseDto.MgDLGlucoseDto.serializer()
            units.contains("mmol", ignoreCase = true) -> GlucoseDto.MmolGlucoseDto.serializer()
            else                                      -> throw SerializationException("Unknown glucose units: $units")
        }
    }
}