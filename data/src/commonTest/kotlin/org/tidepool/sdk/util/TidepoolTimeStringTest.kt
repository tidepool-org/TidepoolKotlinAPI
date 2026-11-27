package org.tidepool.sdk.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class TidepoolTimeStringTest {

    @Test
    fun `whole seconds still get three fractional digits`() {
        assertEquals("2023-11-14T22:13:20.000Z", Instant.fromEpochSeconds(1_700_000_000).toTidepoolTimeString())
    }

    @Test
    fun `milliseconds are kept`() {
        val instant = Instant.fromEpochSeconds(1_700_000_000, 120_000_000)
        assertEquals("2023-11-14T22:13:20.120Z", instant.toTidepoolTimeString())
    }

    @Test
    fun `sub-millisecond precision rounds to the nearest millisecond`() {
        assertEquals(
            "2023-11-14T22:13:20.123Z",
            Instant.fromEpochSeconds(1_700_000_000, 123_456_789).toTidepoolTimeString(),
        )
        assertEquals(
            "2023-11-14T22:13:20.124Z",
            Instant.fromEpochSeconds(1_700_000_000, 123_500_000).toTidepoolTimeString(),
        )
    }

    @Test
    fun `rounding up carries into the next second`() {
        assertEquals(
            "2023-11-14T22:13:21.000Z",
            Instant.fromEpochSeconds(1_700_000_000, 999_600_000).toTidepoolTimeString(),
        )
    }

    @Test
    fun `md5Base64 matches a known digest`() {
        assertEquals("1B2M2Y8AsgTpgAmY7PhCfg==", md5Base64(ByteArray(0)))
    }
}
