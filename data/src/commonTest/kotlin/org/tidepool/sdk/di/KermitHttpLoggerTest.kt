package org.tidepool.sdk.di

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KermitHttpLoggerTest {

    private data class Line(val severity: Severity, val tag: String, val message: String)

    private val lines = mutableListOf<Line>()

    @BeforeTest
    fun captureKermit() {
        Logger.setLogWriters(
            object : LogWriter() {
                override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
                    lines += Line(severity, tag, message)
                }
            },
        )
        Logger.setMinSeverity(Severity.Verbose)
    }

    @AfterTest
    fun restoreKermit() {
        Logger.setLogWriters(platformLogWriter())
    }

    @Test
    fun `user and data set IDs in a path are replaced`() {
        assertEquals(
            "REQUEST: https://api.tidepool.org/v1/users/***/data_sets",
            KermitHttpLogger.redactIds("REQUEST: https://api.tidepool.org/v1/users/1a2b3c4d5e/data_sets"),
        )
        assertEquals(
            "FROM: https://api.tidepool.org/v1/data_sets/***/data",
            KermitHttpLogger.redactIds(
                "FROM: https://api.tidepool.org/v1/data_sets/0123456789abcdef0123456789abcdef/data",
            ),
        )
    }

    @Test
    fun `an ID before a query or at the end of a line is replaced`() {
        assertEquals(
            "https://api.tidepool.org/v1/users/***?limit=1\nMETHOD: GET /metadata/***",
            KermitHttpLogger.redactIds(
                "https://api.tidepool.org/v1/users/1a2b3c4d5e?limit=1\nMETHOD: GET /metadata/abcdef01234",
            ),
        )
    }

    @Test
    fun `short segments, words and values outside a path are kept`() {
        val message = "REQUEST: https://api.tidepool.org/v1/data_sets/cafe/data\nBODY: {\"time\": 1700000000000}"

        assertEquals(message, KermitHttpLogger.redactIds(message))
    }

    @Test
    fun `lines go to Kermit at info, tagged as Ktor's Android logger tags them, with IDs replaced`() {
        KermitHttpLogger().log("REQUEST: https://api.tidepool.org/v1/users/1a2b3c4d5e/data_sets")

        assertEquals(
            listOf(Line(Severity.Info, "Ktor Client", "REQUEST: https://api.tidepool.org/v1/users/***/data_sets")),
            lines,
        )
    }
}
