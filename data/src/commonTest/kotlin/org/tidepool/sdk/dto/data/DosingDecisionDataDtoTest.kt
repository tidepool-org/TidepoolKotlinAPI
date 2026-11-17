package org.tidepool.sdk.dto.data

import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.di.createTidepoolJson
import org.tidepool.sdk.model.data.DosingDecisionData
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * [DosingDecisionData.RecommendedBasal.duration] is milliseconds, kept as a `Double` on the
 * domain side for arithmetic, but TidepoolKit's `TDosingDecisionDatum` sends it as an integer
 * (`Int(duration.milliseconds)`), not a floating point number of minutes.
 */
class DosingDecisionDataDtoTest {

    private val json = createTidepoolJson()

    @Test
    fun `recommendedBasal duration is sent as an integer number of milliseconds`() {
        val decision = DosingDecisionDataDto(
            reason = "loop",
            recommendedBasal = DosingDecisionDataDto.RecommendedBasalDto(rate = 0.5, duration = 1_800_123L),
        )

        val encoded = json.encodeToJsonElement(decision).jsonObject
            .getValue("recommendedBasal").jsonObject
            .getValue("duration")

        assertEquals("1800123", encoded.jsonPrimitive.content)
    }

    @Test
    fun `recommendedBasal duration round-trips through the domain model as a Double`() {
        val dto = DosingDecisionDataDto.RecommendedBasalDto(rate = 0.5, duration = 1_800_000L)

        val domain = dto.toDomain()
        assertEquals(1_800_000.0, domain.duration)

        val roundTripped = domain.toDto()
        assertEquals(1_800_000L, roundTripped.duration)
    }

    @Test
    fun `a fractional millisecond duration rounds to the nearest integer`() {
        val domain = DosingDecisionData.RecommendedBasal(rate = 0.5, duration = 1_800_000.6)

        assertEquals(1_800_001L, domain.toDto().duration)
    }
}
