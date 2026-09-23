package org.tidepool.sdk.dto.prescription

import kotlin.test.Test
import kotlin.test.assertEquals

class PrescriptionDtoTest {

    // NEMO-470: the platform's `start` field for schedule entries is milliseconds-of-day
    // (0..86400000, see pump.BasalRateStartArray et al.), not seconds. Regression for a crash
    // in Long.toClockLabel() (LocalTime.ofSecondOfDay requires 0..86399) when a claimed
    // prescription had a non-zero schedule start time. Confirmed against a real claim response
    // from qa1.development.tidepool.org: carbohydrateRatioSchedule containing
    // {"amount":9,"start":43200000} — the exact value from the Crashlytics report.
    @Test
    fun `toDomain converts millisecond schedule start times to seconds`() {
        val dto = InitialSettingsDto(
            glucoseUnit = "mg/dL",
            glucoseTargetSchedule = listOf(
                GlucoseTargetEntryDto(startMillis = 0, low = 100.0, high = 120.0),
                GlucoseTargetEntryDto(startMillis = 21_600_000, low = 110.0, high = 130.0),
            ),
            basalRateSchedule = listOf(
                BasalRateEntryDto(startMillis = 0, rate = 1.0),
                BasalRateEntryDto(startMillis = 54_000_000, rate = 0.85),
            ),
            carbRatioSchedule = listOf(
                CarbRatioEntryDto(startMillis = 32_400_000, ratio = 10.0),
                // Exact value from the Crashlytics report for NEMO-470: 43_200_000 ms = noon.
                CarbRatioEntryDto(startMillis = 43_200_000, ratio = 12.0),
            ),
            insulinSensitivitySchedule = listOf(ISFEntryDto(startMillis = 9 * 3_600_000L, amount = 45.0)),
        )

        val domain = dto.toDomain()

        assertEquals(0L, domain.glucoseTargetSchedule[0].startSeconds)
        assertEquals(6 * 3600L, domain.glucoseTargetSchedule[1].startSeconds)
        assertEquals(0L, domain.basalRateSchedule[0].startSeconds)
        assertEquals(54_000L, domain.basalRateSchedule[1].startSeconds)
        assertEquals(9 * 3600L, domain.carbRatioSchedule[0].startSeconds)
        assertEquals(12 * 3600L, domain.carbRatioSchedule[1].startSeconds)
        assertEquals(9 * 3600L, domain.insulinSensitivitySchedule[0].startSeconds)
    }

    // The server's validator inclusively accepts the documented-as-exclusive upper bound
    // (86_400_000, see BasalRateStartStartMaximum in tidepool-org/platform), which is equivalent
    // to midnight. Without wrapping, this converts to 86_400 seconds, which
    // LocalTime.ofSecondOfDay() rejects — the crash would just move to this boundary instead of
    // being fixed.
    @Test
    fun `toDomain wraps the server-legal boundary value 86_400_000 to midnight`() {
        val dto = InitialSettingsDto(
            glucoseUnit = "mg/dL",
            insulinSensitivitySchedule = listOf(ISFEntryDto(startMillis = 86_400_000, amount = 45.0)),
        )

        val domain = dto.toDomain()

        assertEquals(0L, domain.insulinSensitivitySchedule[0].startSeconds)
    }
}
