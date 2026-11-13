package org.tidepool.sdk.dto.data

import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.di.createTidepoolJson
import org.tidepool.sdk.dto.BloodGlucoseDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Settings datums (pumpSettings, cgmSettings, controllerSettings) round-trip their new fields
 * through JSON with the names TidepoolKit uses, and omit everything when null.
 */
class SettingsDataDtoTest {

    private val json = createTidepoolJson()

    @Test
    fun `an empty pumpSettings sends only the base fields`() {
        val datum = PumpSettingsDataDto()

        val encoded = json.encodeToJsonElement(datum).jsonObject
        assertFalse("basal" in encoded)
        assertFalse("units" in encoded)
        assertFalse("overridePresets" in encoded)
    }

    @Test
    fun `pumpSettings basal rate maximum and schedules use TidepoolKit's key names`() {
        val datum = PumpSettingsDataDto(
            activeScheduleName = "Default",
            automatedDelivery = true,
            basal = PumpSettingsDataDto.BasalDto(
                rateMaximum = PumpSettingsDataDto.BasalDto.RateMaximumDto(
                    value = 3.0,
                    units = PumpSettingsDataDto.BasalDto.RateUnitsDto.UnitsPerHour,
                ),
            ),
            basalRateSchedules = mapOf(
                "Default" to listOf(PumpSettingsDataDto.ScheduleStartDto(start = 0L, rate = 0.5)),
            ),
        )

        val encoded = json.encodeToJsonElement(datum).jsonObject
        assertEquals("Default", encoded.getValue("activeSchedule").jsonPrimitive.content)
        assertEquals("true", encoded.getValue("automatedDelivery").jsonPrimitive.content)
        val rateMaximum = encoded.getValue("basal").jsonObject.getValue("rateMaximum").jsonObject
        assertEquals("3.0", rateMaximum.getValue("value").jsonPrimitive.content)
        assertEquals("Units/hour", rateMaximum.getValue("units").jsonPrimitive.content)
        val schedule = encoded.getValue("basalSchedules").jsonObject.getValue("Default").jsonArray[0].jsonObject
        assertEquals("0.5", schedule.getValue("rate").jsonPrimitive.content)
    }

    @Test
    fun `pumpSettings units nest bg, carb and insulin`() {
        val datum = PumpSettingsDataDto(
            units = PumpSettingsDataDto.UnitsDto(
                bloodGlucose = BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter,
                carbohydrate = DosingDecisionDataDto.UnitsDto.CarbDto.Grams,
                insulin = InsulinDto.UnitsDto.Units,
            ),
        )

        val units = json.encodeToJsonElement(datum).jsonObject.getValue("units").jsonObject
        assertEquals("mg/dL", units.getValue("bg").jsonPrimitive.content)
        assertEquals("grams", units.getValue("carb").jsonPrimitive.content)
        assertEquals("units", units.getValue("insulin").jsonPrimitive.content)
    }

    @Test
    fun `an empty cgmSettings and controllerSettings send only the base fields`() {
        val cgm = CgmSettingsDataDto()
        val controller = ControllerSettingsDataDto()

        assertFalse("units" in json.encodeToJsonElement(cgm).jsonObject)
        assertFalse("device" in json.encodeToJsonElement(controller).jsonObject)
    }

    @Test
    fun `controllerSettings device and notifications use TidepoolKit's key names`() {
        val datum = ControllerSettingsDataDto(
            device = ControllerSettingsDataDto.DeviceDto(name = "Pixel", model = "9"),
            notifications = ControllerSettingsDataDto.NotificationsDto(
                authorization = ControllerSettingsDataDto.NotificationsDto.AuthorizationDto.Authorized,
                alertStyle = ControllerSettingsDataDto.NotificationsDto.AlertStyleDto.Banner,
            ),
        )

        val encoded = json.encodeToJsonElement(datum).jsonObject
        assertEquals("Pixel", encoded.getValue("device").jsonObject.getValue("name").jsonPrimitive.content)
        assertEquals(
            "authorized",
            encoded.getValue("notifications").jsonObject.getValue("authorization").jsonPrimitive.content,
        )
        assertEquals(
            "banner",
            encoded.getValue("notifications").jsonObject.getValue("alertStyle").jsonPrimitive.content,
        )
    }
}
