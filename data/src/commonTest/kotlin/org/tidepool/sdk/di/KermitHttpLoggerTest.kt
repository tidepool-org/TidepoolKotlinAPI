package org.tidepool.sdk.di

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KermitHttpLoggerTest {

    private data class Line(val severity: Severity, val tag: String, val message: String)

    private val lines = mutableListOf<Line>()

    private lateinit var savedWriters: List<LogWriter>
    private lateinit var savedMinSeverity: Severity

    @BeforeTest
    fun captureKermit() {
        savedWriters = Logger.config.logWriterList
        savedMinSeverity = Logger.config.minSeverity
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
        Logger.setLogWriters(savedWriters)
        Logger.setMinSeverity(savedMinSeverity)
    }

    @Test
    fun `lines go to Kermit at info, tagged as Ktor's Android logger tags them`() {
        KermitHttpLogger().log("REQUEST: https://api.tidepool.org/v1/users/***/data_sets")

        assertEquals(
            listOf(Line(Severity.Info, "Ktor Client", "REQUEST: https://api.tidepool.org/v1/users/***/data_sets")),
            lines,
        )
    }
}
