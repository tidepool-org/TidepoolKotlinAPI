package org.tidepool.sdk.dto.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.tidepool.sdk.database.entity.data.toDto
import org.tidepool.sdk.database.entity.data.toEntity
import org.tidepool.sdk.di.createTidepoolJson
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.data.DeviceEventData
import org.tidepool.sdk.model.data.Origin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.time.Instant

/**
 * `deviceEvent/status` and `deviceEvent/pumpSettingsOverride` encode as TidepoolKit's
 * TStatusDeviceEventDatum and TPumpSettingsOverrideDeviceEventDatum do, and survive the outbox.
 */
class DeviceEventDataDtoTest {

    private val json = createTidepoolJson()
    private val origin = Origin(id = "sync-1", name = "org.tidepool.loop", type = "application", version = "1.0")

    @Test
    fun `a suspend encodes its status and its reason keyed by that status`() {
        val datum = DeviceEventData(
            id = "status-1",
            subType = DeviceEventData.SubType.Status,
            status = DeviceEventData.Status.Suspended,
            statusReason = DeviceEventData.StatusReason.Manual,
        ).toDto()

        val encoded = json.encodeToJsonElement(datum).jsonObject

        assertEquals(Json.parseToJsonElement("\"status\""), encoded.getValue("subType"))
        assertEquals(Json.parseToJsonElement("\"suspended\""), encoded.getValue("status"))
        assertEquals(Json.parseToJsonElement("""{"suspended":"manual"}"""), encoded.getValue("reason"))
        assertFalse("duration" in encoded)
        assertFalse("overrideType" in encoded)
    }

    @Test
    fun `a preset override encodes its target, units, scale factors and millisecond duration`() {
        val datum = DeviceEventData(
            id = "override-1",
            subType = DeviceEventData.SubType.PumpSettingsOverride,
            overrideType = DeviceEventData.OverrideType.Preset,
            overridePreset = "Running",
            method = DeviceEventData.Method.Manual,
            durationMillis = 3_600_000L,
            bloodGlucoseTarget = BloodGlucose.Target(target = null, range = null, low = 140.0, high = 160.0),
            bloodGlucoseUnits = BloodGlucose.Units.MilligramsPerDeciliter,
            basalRateScaleFactor = 0.5,
            carbRatioScaleFactor = 2.0,
            insulinSensitivityScaleFactor = 2.0,
        ).toDto()

        val encoded = json.encodeToJsonElement(datum).jsonObject

        assertEquals(Json.parseToJsonElement("\"pumpSettingsOverride\""), encoded.getValue("subType"))
        assertEquals(Json.parseToJsonElement("\"preset\""), encoded.getValue("overrideType"))
        assertEquals(Json.parseToJsonElement("\"Running\""), encoded.getValue("overridePreset"))
        assertEquals(Json.parseToJsonElement("\"manual\""), encoded.getValue("method"))
        assertEquals(Json.parseToJsonElement("3600000"), encoded.getValue("duration"))
        assertEquals(Json.parseToJsonElement("""{"low":140.0,"high":160.0}"""), encoded.getValue("bgTarget"))
        assertEquals(Json.parseToJsonElement("""{"bg":"mg/dL"}"""), encoded.getValue("units"))
        assertEquals(Json.parseToJsonElement("0.5"), encoded.getValue("basalRateScaleFactor"))
        assertEquals(Json.parseToJsonElement("2.0"), encoded.getValue("carbRatioScaleFactor"))
        assertEquals(Json.parseToJsonElement("2.0"), encoded.getValue("insulinSensitivityScaleFactor"))
        assertFalse("status" in encoded)
    }

    @Test
    fun `status and override datums round-trip through the outbox entity`() {
        val status = DeviceEventData(
            id = "status-1",
            time = Instant.fromEpochSeconds(1_700_000_000),
            subType = DeviceEventData.SubType.Status,
            origin = origin.copy(id = "raw-1:deviceEvent/status"),
            payload = DeviceEventData.Payload(syncIdentifier = "raw-1"),
            status = DeviceEventData.Status.Resumed,
            statusReason = DeviceEventData.StatusReason.Automatic,
        )
        val override = DeviceEventData(
            id = "override-1",
            time = Instant.fromEpochSeconds(1_700_000_000),
            subType = DeviceEventData.SubType.PumpSettingsOverride,
            origin = origin.copy(id = "sync-1:deviceEvent/pumpSettingsOverride"),
            payload = DeviceEventData.Payload(syncIdentifier = "sync-1"),
            overrideType = DeviceEventData.OverrideType.Preprandial,
            method = DeviceEventData.Method.Manual,
            durationMillis = 1_800_000L,
            bloodGlucoseTarget = BloodGlucose.Target(target = null, range = null, low = 80.0, high = 90.0),
            bloodGlucoseUnits = BloodGlucose.Units.MilligramsPerDeciliter,
        )

        listOf(status, override).forEach { datum ->
            assertEquals(datum.toDto(), datum.toDto().toEntity().toDto())
            assertEquals(datum, datum.toDto().toEntity().toDto().toDomain())
        }
    }
}
