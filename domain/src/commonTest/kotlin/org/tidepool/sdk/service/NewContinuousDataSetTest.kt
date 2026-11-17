package org.tidepool.sdk.service

import kotlin.time.Instant
import java.util.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.tidepool.sdk.ClientInfo

class NewContinuousDataSetTest {

    private val prague = TimeZone.getTimeZone("Europe/Prague")
    private val clientInfo = ClientInfo(name = "org.tidepool.loop", version = "TEST")

    // rawOffset ignores DST, so a summer data set in Europe/Prague was created with +60 instead of +120.
    @Test
    fun `timeZoneOffset includes daylight saving time`() {
        val dataSet = newContinuousDataSet(Instant.parse("2026-09-29T10:18:32Z"), prague, clientInfo)

        assertEquals("Europe/Prague", dataSet.timezone)
        assertEquals(120, dataSet.timeZoneOffset)
    }

    @Test
    fun `timeZoneOffset is standard time in winter`() {
        val dataSet = newContinuousDataSet(Instant.parse("2026-01-15T10:18:32Z"), prague, clientInfo)

        assertEquals(60, dataSet.timeZoneOffset)
    }

    @Test
    fun `client name and version come from ClientInfo, with no device identifiers`() {
        val dataSet = newContinuousDataSet(Instant.parse("2026-01-15T10:18:32Z"), prague, clientInfo)

        assertEquals("org.tidepool.loop", dataSet.client?.name)
        assertEquals("TEST", dataSet.client?.version)
        assertNull(dataSet.deviceId)
        assertNull(dataSet.deviceManufacturers)
        assertNull(dataSet.deviceSerialNumber)
        assertNull(dataSet.deviceModel)
    }
}
