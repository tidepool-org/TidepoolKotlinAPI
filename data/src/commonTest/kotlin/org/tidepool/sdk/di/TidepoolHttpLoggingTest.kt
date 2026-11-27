package org.tidepool.sdk.di

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * The session token must never reach logcat, and upload bodies (health data) only when the host
 * opts into verbose logging.
 */
class TidepoolHttpLoggingTest {

    private val logged = mutableListOf<String>()

    @Test
    fun `default logging shows the request but not its body or the token`() = runTest {
        upload(LogLevel.INFO)

        val log = logged.joinToString("\n")
        assertTrue(log.contains("v1/data_sets/data-set/data"), log)
        assertFalse(log.contains("glucose-reading"), log)
        assertFalse(log.contains("secret-token"), log)
    }

    @Test
    fun `verbose logging shows the body but masks the token`() = runTest {
        upload(LogLevel.ALL)

        val log = logged.joinToString("\n")
        assertTrue(log.contains("glucose-reading"), log)
        assertTrue(log.contains("X-Tidepool-Session-Token: ***"), log)
        assertFalse(log.contains("secret-token"), log)
    }

    @Test
    fun `identifiers in the URL are blanked before the logger gets the line`() = runTest {
        upload(LogLevel.INFO, url = "https://test.invalid/v1/users/1a2b3c4d5e/data_sets?email=person%40example.org&limit=1")

        val log = logged.joinToString("\n")
        assertTrue(log.contains("v1/users/***/data_sets?email=***&limit=1"), log)
        assertFalse(log.contains("1a2b3c4d5e"), log)
        assertFalse(log.contains("example.org"), log)
    }

    private suspend fun upload(logLevel: LogLevel, url: String = "https://test.invalid/v1/data_sets/data-set/data") {
        val client = createTidepoolHttpClient(
            engine = MockEngine {
                respond(
                    content = "{}",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
            tidepoolJson = createTidepoolJson(),
            httpLogger = object : Logger {
                override fun log(message: String) {
                    logged += message
                }
            },
            logLevel = logLevel,
        )
        client.post(url) {
            header("X-Tidepool-Session-Token", "secret-token")
            setBody("""[{"type":"cbg","id":"glucose-reading"}]""")
        }
    }
}
