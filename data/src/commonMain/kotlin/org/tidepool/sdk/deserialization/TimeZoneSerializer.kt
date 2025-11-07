package org.tidepool.sdk.deserialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.TimeZone

object TimeZoneSerializer : KSerializer<TimeZone> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("TimeZone", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: TimeZone,
    ) {
        encoder.encodeString(value.id)
    }

    override fun deserialize(decoder: Decoder): TimeZone =
        TimeZone.getTimeZone(decoder.decodeString())
}