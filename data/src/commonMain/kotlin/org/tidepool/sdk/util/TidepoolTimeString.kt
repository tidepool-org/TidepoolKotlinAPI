package org.tidepool.sdk.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Instant

private const val MILLIS_PER_SECOND = 1_000L
private const val NANOS_PER_MILLI = 1_000_000
private const val FRACTION_DIGITS = 3

/**
 * UTC ISO-8601 with exactly three fractional digits and a `Z` suffix, rounded to the nearest
 * millisecond. Port of iOS `Date.timeString` (`.withInternetDateTime, .withFractionalSeconds`).
 */
internal fun Instant.toTidepoolTimeString(): String {
    val millis = epochSeconds * MILLIS_PER_SECOND +
        (nanosecondsOfSecond + NANOS_PER_MILLI / 2) / NANOS_PER_MILLI
    val iso = Instant.fromEpochMilliseconds(millis).toString().removeSuffix("Z")
    val seconds = iso.substringBefore('.')
    val fraction = iso.substringAfter('.', "").padEnd(FRACTION_DIGITS, '0')
    return "$seconds.${fraction}Z"
}

/** Writes an [Instant] as [toTidepoolTimeString]; reads any ISO-8601 instant. */
internal object TidepoolTimeStringSerializer : KSerializer<Instant> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TidepoolTimeString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toTidepoolTimeString())
    }

    override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}
