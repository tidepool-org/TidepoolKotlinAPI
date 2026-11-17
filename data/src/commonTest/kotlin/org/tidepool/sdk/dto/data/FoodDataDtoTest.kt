package org.tidepool.sdk.dto.data

import kotlin.time.Instant
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.di.createTidepoolJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * [FoodDataDto] gains a payload (syncIdentifier, syncVersion and the platform's bookkeeping
 * times), serialized the same way as the datum's own [FoodDataDto.time].
 */
class FoodDataDtoTest {

    private val json = createTidepoolJson()

    @Test
    fun `payload is omitted when null`() {
        val datum = FoodDataDto()

        val encoded = json.encodeToJsonElement(datum).jsonObject
        assertFalse("payload" in encoded)
    }

    @Test
    fun `payload's times are sent in the same format as the datum's own time`() {
        val time = Instant.parse("2026-11-10T09:24:00Z")
        val datum = FoodDataDto(
            payload = FoodDataDto.PayloadDto(
                syncIdentifier = "sync-1",
                syncVersion = 1,
                userCreatedDate = time,
                userUpdatedDate = time,
            ),
        )

        val payload = json.encodeToJsonElement(datum).jsonObject.getValue("payload").jsonObject
        assertEquals("sync-1", payload.getValue("syncIdentifier").jsonPrimitive.content)
        assertEquals("1", payload.getValue("syncVersion").jsonPrimitive.content)
        assertEquals("2026-11-10T09:24:00Z", payload.getValue("userCreatedDate").jsonPrimitive.content)
        assertEquals("2026-11-10T09:24:00Z", payload.getValue("userUpdatedDate").jsonPrimitive.content)
        assertFalse("userDeletedDate" in payload)
        assertFalse("addedDate" in payload)
        assertFalse("supercededDate" in payload)
    }
}
