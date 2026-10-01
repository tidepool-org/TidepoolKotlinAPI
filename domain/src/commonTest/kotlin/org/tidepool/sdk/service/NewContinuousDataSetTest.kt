package org.tidepool.sdk.service

import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class NewContinuousDataSetTest {

    private val prague = TimeZone.getTimeZone("Europe/Prague")

    // rawOffset ignores DST, so a summer data set in Europe/Prague was created with +60 instead of +120.
    @Test
    fun `timeZoneOffset includes daylight saving time`() {
        val dataSet = newContinuousDataSet(Instant.parse("2026-09-29T10:18:32Z"), prague)

        assertEquals("Europe/Prague", dataSet.timezone)
        assertEquals(120, dataSet.timeZoneOffset)
    }

    @Test
    fun `timeZoneOffset is standard time in winter`() {
        val dataSet = newContinuousDataSet(Instant.parse("2026-01-15T10:18:32Z"), prague)

        assertEquals(60, dataSet.timeZoneOffset)
    }
}
