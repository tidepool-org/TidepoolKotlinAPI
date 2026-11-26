package org.tidepool.sdk.repository

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.Environment
import org.tidepool.sdk.di.createTidepoolHttpClient
import org.tidepool.sdk.di.createTidepoolJson
import org.tidepool.sdk.dto.blob.DeviceLogTypeDto
import org.tidepool.sdk.model.blob.DeviceLogContent
import org.tidepool.sdk.model.blob.DeviceLogType
import org.tidepool.sdk.util.md5Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

/**
 * Pins the device-log upload on the wire: the posted bytes, the `Digest` header over exactly those
 * bytes, and the millisecond-precision times, so a change to the shared Json config shows up here.
 */
class BlobRepositoryImplDeviceLogsTest {

    private val requests = mutableListOf<HttpRequestData>()
    private val bodies = mutableListOf<ByteArray>()

    private val start = Instant.fromEpochSeconds(1_700_000_000)
    private val end = Instant.fromEpochSeconds(1_700_003_600)

    private val logs = listOf(
        DeviceLogContent(
            type = DeviceLogType.Send,
            managerIdentifier = "mgr",
            deviceIdentifier = "dev-1",
            timestamp = Instant.fromEpochSeconds(1_700_000_000, 123_456_789),
            message = "tx 0x01/02",
        ),
        DeviceLogContent(
            type = DeviceLogType.DelegateResponse,
            managerIdentifier = "mgr2",
            deviceIdentifier = "unknown",
            timestamp = Instant.fromEpochSeconds(1_700_000_001),
            message = "ok",
        ),
    )

    private val expectedBody =
        """[{"deviceIdentifier":"dev-1","managerIdentifier":"mgr","message":"tx 0x01/02",""" +
            """"timestamp":"2023-11-14T22:13:20.123Z","type":"send"},""" +
            """{"deviceIdentifier":"unknown","managerIdentifier":"mgr2","message":"ok",""" +
            """"timestamp":"2023-11-14T22:13:21.000Z","type":"delegateResponse"}]"""

    @Test
    fun `the posted body is the exact sorted-key JSON`() = runTest {
        upload()

        assertEquals(expectedBody, bodies.single().decodeToString())
    }

    @Test
    fun `the Digest header is the MD5 of the exact posted bytes`() = runTest {
        upload()

        val request = requests.single()
        assertEquals("MD5=9V58EqXQ01iTWRwvWNvMwA==", request.headers["Digest"])
        assertEquals("MD5=" + md5Base64(bodies.single()), request.headers["Digest"])
    }

    @Test
    fun `request line and time headers are exact`() = runTest {
        upload()

        val request = requests.single()
        assertEquals(HttpMethod.Post, request.method)
        assertEquals("/v1/users/user-1/device_logs", request.url.encodedPath)
        assertEquals("2023-11-14T22:13:20.000Z", request.headers["X-Logs-Start-At-Time"])
        assertEquals("2023-11-14T23:13:20.000Z", request.headers["X-Logs-End-At-Time"])
        assertEquals("token", request.headers["X-Tidepool-Session-Token"])
    }

    @Test
    fun `Content-Type is application json and appears once`() = runTest {
        upload()

        // Ktor moves Content-Type out of the header map into the body's content type, so the
        // request carries exactly one: the body's. A second one in `headers` would be a duplicate.
        val request = requests.single()
        assertEquals(emptyList(), request.headers.getAll(HttpHeaders.ContentType).orEmpty())
        assertEquals(ContentType.Application.Json, request.body.contentType?.withoutParameters())
    }

    @Test
    fun `list query parameters carry millisecond precision`() = runTest {
        val result = repository(listBody = "[]").listDeviceLogs("user-1", start, end, "token")

        assertTrue(result.isSuccess)
        val url = requests.single().url
        assertEquals("2023-11-14T22:13:20.000Z", url.parameters["startAtTime"])
        assertEquals("2023-11-14T23:13:20.000Z", url.parameters["endAtTime"])
    }

    @Test
    fun `all six log types serialize to their wire strings`() {
        val json = createTidepoolJson()
        val wire = DeviceLogTypeDto.entries.map { json.encodeToString(DeviceLogTypeDto.serializer(), it) }
        assertEquals(
            listOf("send", "receive", "error", "delegate", "delegateResponse", "connection")
                .map { "\"$it\"" },
            wire,
        )
    }

    private suspend fun upload() {
        val result = repository().uploadDeviceLogs("user-1", start, end, logs, "token")
        assertTrue(result.isSuccess, result.toString())
    }

    private fun repository(listBody: String = uploadResponse): BlobRepositoryImpl {
        val json = createTidepoolJson()
        return BlobRepositoryImpl(
            environmentRepository = TestEnvironmentRepository,
            httpClient = createTidepoolHttpClient(
                engine = MockEngine { request ->
                    requests += request
                    bodies += request.body.toByteArray()
                    respond(content = listBody, status = HttpStatusCode.OK, headers = jsonHeaders)
                },
                tidepoolJson = json,
                httpLogger = NoLogger,
                logLevel = LogLevel.NONE,
            ),
            json = json,
        )
    }

    private object NoLogger : Logger {
        override fun log(message: String) = Unit
    }

    private object TestEnvironmentRepository : EnvironmentRepository {
        override suspend fun getEnvironmentOptions() = Result.success(listOf(Environment.Production))
        override fun setEnvironment(environment: Environment) = Unit
        override fun getEnvironment() = Environment("test.invalid")
    }

    private companion object {
        val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")
        const val uploadResponse =
            """{"id":"b","userId":"user-1","digestMD5":"x","mediaType":"application/json","size":1,""" +
                """"createdTime":"2023-11-14T22:13:20Z","startAtTime":"2023-11-14T22:13:20Z",""" +
                """"endAtTime":"2023-11-14T23:13:20Z"}"""
    }
}
