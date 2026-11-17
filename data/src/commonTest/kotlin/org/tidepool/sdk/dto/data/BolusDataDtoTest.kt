package org.tidepool.sdk.dto.data

import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.database.entity.data.toDto
import org.tidepool.sdk.database.entity.data.toEntity
import org.tidepool.sdk.di.createTidepoolJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

/**
 * [BolusDataDto.deliveryContext] is nullable: iOS sends none for a bolus without one, instead of
 * a placeholder value.
 */
class BolusDataDtoTest {

    private val json = createTidepoolJson()

    @Test
    fun `a null deliveryContext is omitted from the uploaded datum`() {
        val datum = BolusDataDto(deliveryContext = null)

        val encoded = json.encodeToJsonElement(datum).jsonObject
        assertFalse("deliveryContext" in encoded)
    }

    @Test
    fun `a present deliveryContext is still sent`() {
        val datum = BolusDataDto(deliveryContext = DeliveryContextDto.Algorithm)

        val encoded = json.encodeToJsonElement(datum).jsonObject
        assertEquals("algorithm", encoded.getValue("deliveryContext").jsonPrimitive.content)
    }

    @Test
    fun `a null deliveryContext round-trips through the outbox entity`() {
        val datum = BolusDataDto(deliveryContext = null, normal = 1.0)

        val read = datum.toEntity().toDto()

        assertNull(read.deliveryContext)
        assertEquals(1.0, read.normal)
    }

    @Test
    fun `expectedNormal is sent alongside normal`() {
        val datum = BolusDataDto(deliveryContext = DeliveryContextDto.Algorithm, normal = 1.0, expectedNormal = 1.5)

        val encoded = json.encodeToJsonElement(datum).jsonObject
        assertEquals("1.5", encoded.getValue("expectedNormal").jsonPrimitive.content)
    }
}
