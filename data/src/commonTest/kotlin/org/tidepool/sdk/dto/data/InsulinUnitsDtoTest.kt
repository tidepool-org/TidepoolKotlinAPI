package org.tidepool.sdk.dto.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.di.createTidepoolJson
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * [InsulinDto.UnitsDto] is the one insulin-units enum shared by the insulin dose, pump status
 * reservoir, pump settings insulin units/amountMaximum and dosing decision units.insulin.
 * Platform only accepts "Units" (data/types/insulin/dose.go, data/types/status/pump/reservoir.go).
 */
class InsulinUnitsDtoTest {

    private val json = createTidepoolJson()

    @Test
    fun `insulin dose units encode as Units`() {
        val dose = DoseDto(
            units = InsulinDto.UnitsDto.Units,
            total = 1.0,
            food = null,
            correction = null,
            active = null,
        )

        val encoded = json.encodeToJsonElement(dose).jsonObject
        assertEquals("Units", encoded.getValue("units").jsonPrimitive.content)
    }

    @Test
    fun `pump status reservoir units encode as Units`() {
        val reservoir = PumpStatusDataDto.ReservoirDto(units = InsulinDto.UnitsDto.Units)

        val encoded = json.encodeToJsonElement(reservoir).jsonObject
        assertEquals("Units", encoded.getValue("units").jsonPrimitive.content)
    }

    @Test
    fun `a cached dose JSON string queued under the old lowercase spelling still decodes`() {
        // Mirrors InsulinDataEntity.toDto(), which decodes the stored `dose` column with the
        // default Json instance, not createTidepoolJson().
        val dose = Json.decodeFromString<DoseDto>(
            """{"units":"units","total":1.0,"food":null,"correction":null,"active":null}""",
        )

        assertEquals(InsulinDto.UnitsDto.Units, dose.units)
    }

    @Test
    fun `a cached reservoir JSON string queued under the old lowercase spelling still decodes`() {
        // Mirrors PumpStatusDataEntity.toDto(), which decodes the stored `reservoir` column with
        // the default Json instance.
        val reservoir = Json.decodeFromString<PumpStatusDataDto.ReservoirDto>(
            """{"units":"units"}""",
        )

        assertEquals(InsulinDto.UnitsDto.Units, reservoir.units)
    }
}
