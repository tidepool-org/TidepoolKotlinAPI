package org.tidepool.sdk.dto.data

import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.di.createTidepoolJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * [ContinuousGlucoseDataDto] gains origin, payload (syncIdentifier, syncVersion) and deviceId, so
 * an updated reading can replace an earlier upload (the data set deduplicates by origin) and
 * carry the same sync identifier/device as iOS does.
 */
class ContinuousGlucoseDataDtoTest {

    private val json = createTidepoolJson()

    @Test
    fun `origin, payload and deviceId are omitted when null`() {
        val datum = ContinuousGlucoseDataDto()

        val encoded = json.encodeToJsonElement(datum).jsonObject
        assertFalse("origin" in encoded)
        assertFalse("payload" in encoded)
        assertFalse("deviceId" in encoded)
    }

    @Test
    fun `origin, payload and deviceId are sent when present`() {
        val datum = ContinuousGlucoseDataDto(
            origin = OriginDto(id = "sync-1", name = "org.tidepool.loop", type = "application", version = "1.0"),
            payload = ContinuousGlucoseDataDto.PayloadDto(syncIdentifier = "sync-1", syncVersion = 2),
            deviceId = "cgm-device-1",
        )

        val encoded = json.encodeToJsonElement(datum).jsonObject
        assertEquals("sync-1", encoded.getValue("origin").jsonObject.getValue("id").jsonPrimitive.content)
        assertEquals(
            "sync-1",
            encoded.getValue("payload").jsonObject.getValue("syncIdentifier").jsonPrimitive.content,
        )
        assertEquals("2", encoded.getValue("payload").jsonObject.getValue("syncVersion").jsonPrimitive.content)
        assertEquals("cgm-device-1", encoded.getValue("deviceId").jsonPrimitive.content)
    }
}
