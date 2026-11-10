package org.tidepool.sdk.dto.data

import kotlinx.datetime.Instant
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.database.entity.data.toDto
import org.tidepool.sdk.database.entity.data.toEntity
import org.tidepool.sdk.di.createTidepoolJson
import kotlin.test.Test
import kotlin.test.assertEquals

class BaseDataDtoTimeTest {

    private val json = createTidepoolJson()

    // 2026-11-10T09:24:00Z
    private val minute = Instant.fromEpochSeconds(1_794_302_640)
    private val withMillis = Instant.fromEpochMilliseconds(1_794_302_640_123)

    @Test
    fun `uploaded datums send their time in UTC with as many fraction digits as it needs`() {
        val expected = listOf(
            minute to "2026-11-10T09:24:00Z",
            Instant.fromEpochSeconds(1_794_302_640, 100_000_000) to "2026-11-10T09:24:00.100Z",
            withMillis to "2026-11-10T09:24:00.123Z",
            Instant.fromEpochSeconds(1_794_302_640, 123_456_000) to "2026-11-10T09:24:00.123456Z",
            Instant.fromEpochSeconds(1_794_302_640, 123_456_789) to "2026-11-10T09:24:00.123456789Z",
        )

        expected.forEach { (time, text) ->
            val datum = BolusDataDto(time = time, deliveryContext = DeliveryContextDto.Algorithm)
            assertEquals(text, json.encodeToJsonElement(datum).jsonObject["time"]?.jsonPrimitive?.content)
        }
    }

    @Test
    fun `datums downloaded from the platform read their time with or without a fraction or an offset`() {
        val expected = listOf(
            "2026-11-10T09:24:00Z" to minute,
            "2026-11-10T09:24:00.000Z" to minute,
            "2026-11-10T09:24:00.123Z" to withMillis,
            "2026-11-10T10:24:00+01:00" to minute,
        )

        expected.forEach { (text, time) ->
            val datum = json.decodeFromString<ContinuousGlucoseDataDto>("""{"type":"cbg","time":"$text"}""")
            assertEquals(time, datum.time, text)
        }
    }

    @Test
    fun `a stored dosing decision keeps the times of its carbs and insulin on board`() {
        val decision = DosingDecisionDataDto(
            time = minute,
            reason = "loop",
            carbsOnBoard = DosingDecisionDataDto.CarbsOnBoardDto(time = withMillis, amount = 12.5),
            insulinOnBoard = DosingDecisionDataDto.InsulinOnBoardDto(time = withMillis, amount = 1.25),
        )

        val stored = decision.toEntity()
        val read = stored.toDto()

        assertEquals("""{"time":"2026-11-10T09:24:00.123Z","amount":12.5}""", stored.carbsOnBoard)
        assertEquals(minute, read.time)
        assertEquals(decision.carbsOnBoard, read.carbsOnBoard)
        assertEquals(decision.insulinOnBoard, read.insulinOnBoard)
    }
}
