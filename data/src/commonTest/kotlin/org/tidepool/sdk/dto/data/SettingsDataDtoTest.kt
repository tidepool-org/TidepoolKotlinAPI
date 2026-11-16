package org.tidepool.sdk.dto.data

import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.di.createTidepoolJson
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.data.PumpSettingsData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.time.Duration

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
        assertEquals("Units", units.getValue("insulin").jsonPrimitive.content)
    }

    @Test
    fun `an empty cgmSettings and controllerSettings send only the base fields`() {
        val cgm = CgmSettingsDataDto()
        val controller = ControllerSettingsDataDto()

        assertFalse("units" in json.encodeToJsonElement(cgm).jsonObject)
        assertFalse("device" in json.encodeToJsonElement(controller).jsonObject)
    }

    @Test
    fun `pumpSettings schedules and limits encode the way TidepoolKit's TPumpSettingsDatum does`() {
        // Basal entries are BasalRateStart {start, rate}; target entries are TBloodGlucose.StartTarget
        // with low/high beside start; bolus.amountMaximum units is TInsulin.Units "Units".
        val datum = PumpSettingsData(
            id = "id",
            basalRateSchedules = mapOf("Default" to listOf(PumpSettingsData.ScheduleStart(start = 0.0, amount = 1.0))),
            carbohydrateRatioSchedules = mapOf("Default" to listOf(PumpSettingsData.ScheduleStart(start = 0.0, amount = 10.0))),
            bloodGlucoseTargetSchedules = mapOf(
                "Default" to listOf(
                    BloodGlucose.StartTarget(
                        start = Duration.ZERO,
                        target = null,
                        range = null,
                        low = 100.0,
                        high = 110.0,
                    ),
                ),
            ),
            bolus = PumpSettingsData.Bolus(
                amountMaximum = PumpSettingsData.Bolus.AmountMaximum(value = 10.0, units = org.tidepool.sdk.model.data.Insulin.Units.Units),
            ),
        ).toDto()

        val encoded = json.encodeToJsonElement(datum).jsonObject
        val basal = encoded.getValue("basalSchedules").jsonObject.getValue("Default").jsonArray.single().jsonObject
        assertEquals(setOf("start", "rate"), basal.keys)
        val carbRatio = encoded.getValue("carbRatios").jsonObject.getValue("Default").jsonArray.single().jsonObject
        assertEquals(setOf("start", "amount"), carbRatio.keys)
        val target = encoded.getValue("bgTargets").jsonObject.getValue("Default").jsonArray.single().jsonObject
        assertEquals(setOf("start", "low", "high"), target.keys)
        assertEquals("100.0", target.getValue("low").jsonPrimitive.content)
        val amountMaximum = encoded.getValue("bolus").jsonObject.getValue("amountMaximum").jsonObject
        assertEquals("Units", amountMaximum.getValue("units").jsonPrimitive.content)
    }

    @Test
    fun `pumpSettings insulinModel action timings encode as integer seconds, not milliseconds`() {
        // Rapid-acting adult: 10 minute delay, 6 hour duration, 75 minute peak offset.
        val datum = PumpSettingsData(
            id = "id",
            insulinModel = PumpSettingsData.InsulinModel(
                modelType = PumpSettingsData.InsulinModel.ModelType.RapidAdult,
                actionDelay = 600.0,
                actionDuration = 21600.0,
                actionPeakOffset = 4500.0,
            ),
        ).toDto()

        val insulinModel = json.encodeToJsonElement(datum).jsonObject.getValue("insulinModel").jsonObject
        assertEquals("600", insulinModel.getValue("actionDelay").jsonPrimitive.content)
        assertEquals("21600", insulinModel.getValue("actionDuration").jsonPrimitive.content)
        assertEquals("4500", insulinModel.getValue("actionPeakOffset").jsonPrimitive.content)
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
