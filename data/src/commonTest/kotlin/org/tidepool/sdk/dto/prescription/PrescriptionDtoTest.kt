package org.tidepool.sdk.dto.prescription

import kotlin.test.Test
import kotlin.test.assertEquals

class PrescriptionDtoTest {

    // NEMO-470: the platform's `start` field for schedule entries is milliseconds-of-day
    // (0..86400000, see pump.BasalRateStartArray et al.), not seconds. Regression for a crash
    // in Long.toClockLabel() (LocalTime.ofSecondOfDay requires 0..86399) when a claimed
    // prescription had a non-zero schedule start time.
    @Test
    fun `toDomain converts millisecond schedule start times to seconds`() {
        val dto = InitialSettingsDto(
            glucoseUnit = "mg/dL",
            glucoseTargetSchedule = listOf(GlucoseTargetEntryDto(startSeconds = 0, low = 100.0, high = 120.0)),
            basalRateSchedule = listOf(
                BasalRateEntryDto(startSeconds = 0, rate = 1.0),
                BasalRateEntryDto(startSeconds = 54_000_000, rate = 0.85),
            ),
            carbRatioSchedule = listOf(
                CarbRatioEntryDto(startSeconds = 32_400_000, ratio = 10.0),
                // Exact value from the Crashlytics report for NEMO-470: 43_200_000 ms = noon.
                CarbRatioEntryDto(startSeconds = 43_200_000, ratio = 12.0),
            ),
            insulinSensitivitySchedule = listOf(ISFEntryDto(startSeconds = 86_400_000, amount = 45.0)),
        )

        val domain = dto.toDomain()

        assertEquals(0L, domain.basalRateSchedule[0].startSeconds)
        assertEquals(54_000L, domain.basalRateSchedule[1].startSeconds)
        assertEquals(9 * 3600L, domain.carbRatioSchedule[0].startSeconds)
        assertEquals(12 * 3600L, domain.carbRatioSchedule[1].startSeconds)
        assertEquals(86_400L, domain.insulinSensitivitySchedule[0].startSeconds)
    }
}
