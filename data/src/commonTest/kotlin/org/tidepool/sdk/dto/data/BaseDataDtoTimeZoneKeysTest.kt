package org.tidepool.sdk.dto.data

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.di.createTidepoolJson
import java.util.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BaseDataDtoTimeZoneKeysTest {

    private val json = createTidepoolJson()
    private val prague = TimeZone.getTimeZone("Europe/Prague")

    // The platform reads `timezone` / `timezoneOffset` (platform data/types/base.go) and ignores unknown
    // datum keys, so the camel-cased `timeZone` / `timeZoneOffset` we used to send never reached Tidepool.
    @Test
    fun `uploaded datum types send the platform's timezone keys`() {
        // The types DataRepositoryImpl.uploadCachedData drains, in the same shape as DataApi's
        // `@Body data: List<BaseDataDto>`.
        val datums: List<BaseDataDto> = listOf(
            BasalAutomatedDataDto(
                timeZone = prague,
                timeZoneOffset = 120,
                deliveryType = BasalAutomatedDataDto.DeliveryTypeDto.Automated,
                duration = 300_000,
            ),
            BolusDataDto(timeZone = prague, timeZoneOffset = 120, deliveryContext = DeliveryContextDto.Algorithm),
            ContinuousGlucoseDataDto(timeZone = prague, timeZoneOffset = 120),
            DosingDecisionDataDto(timeZone = prague, timeZoneOffset = 120, reason = "loop"),
            FoodDataDto(timeZone = prague, timeZoneOffset = 120),
            InsulinDataDto(
                timeZone = prague,
                timeZoneOffset = 120,
                dose = DoseDto(InsulinDto.UnitsDto.Units, total = 1.0, food = null, correction = null, active = null),
                site = null,
            ),
        )

        json.encodeToJsonElement(datums).jsonArray.zip(datums).forEach { (element, datum) ->
            assertPlatformTimeZoneKeys(element.jsonObject, datum::class.simpleName)
        }
    }

    @Test
    fun `settings and device event datums use the platform's timezone keys`() {
        assertPlatformTimeZoneKeys(CgmSettingsDataDto(timeZone = prague, timeZoneOffset = 120))
        assertPlatformTimeZoneKeys(ControllerSettingsDataDto(timeZone = prague, timeZoneOffset = 120))
        assertPlatformTimeZoneKeys(PumpSettingsDataDto(timeZone = prague, timeZoneOffset = 120))
        assertPlatformTimeZoneKeys(
            DeviceEventDataDto(
                timeZone = prague,
                timeZoneOffset = 120,
                subType = DeviceEventDataDto.SubTypeDto.SensorStart,
            ),
        )
    }

    @Test
    fun `datums downloaded from the platform keep their timezone`() {
        val datum = json.decodeFromString<ContinuousGlucoseDataDto>(
            """{"type":"cbg","timezone":"Europe/Prague","timezoneOffset":120}""",
        )

        assertEquals("Europe/Prague", datum.timeZone?.id)
        assertEquals(120, datum.timeZoneOffset)
    }

    private inline fun <reified T : BaseDataDto> assertPlatformTimeZoneKeys(datum: T) =
        assertPlatformTimeZoneKeys(json.encodeToJsonElement(datum).jsonObject, T::class.simpleName)

    private fun assertPlatformTimeZoneKeys(fields: JsonObject, name: String?) {
        assertEquals("Europe/Prague", fields["timezone"]?.jsonPrimitive?.content, name)
        assertEquals(120, fields["timezoneOffset"]?.jsonPrimitive?.int, name)
        assertFalse("timeZone" in fields, name)
        assertFalse("timeZoneOffset" in fields, name)
    }
}
