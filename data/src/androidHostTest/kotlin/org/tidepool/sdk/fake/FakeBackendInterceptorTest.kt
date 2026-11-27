package org.tidepool.sdk.fake

import kotlin.time.Instant
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.tidepool.sdk.Environment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeBackendInterceptorTest {

    private val recorded = mutableListOf<RecordedRequest>()
    private val fixedNow = Instant.parse("2026-09-29T10:00:00Z")

    // Stands in for the network: counts what reaches it and answers without connecting.
    private var networkCallCount = 0
    private val network = Interceptor { chain ->
        networkCallCount++
        Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("real".toResponseBody())
            .build()
    }

    @Test
    fun `fake host is answered and recorded without reaching the network`() {
        val client = clientWith(FakeBackendInterceptor(recorder = { recorded += it }, now = { fixedNow }))

        val response = client.newCall(
            Request.Builder()
                .url("https://${Environment.Fake.url}/v1/data_sets/fake-data-set/data")
                .post("""[{"type":"cbg"}]""".toRequestBody("application/json".toMediaType()))
                .build()
        ).execute()

        assertEquals(200, response.code)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals("{}", response.body.string())
        assertEquals(0, networkCallCount)
        val request = recorded.single()
        assertEquals(fixedNow, request.time)
        assertEquals("POST", request.method)
        assertEquals("https://${Environment.Fake.url}/v1/data_sets/fake-data-set/data", request.url)
        assertEquals(200, request.status)
        assertEquals("""[{"type":"cbg"}]""", request.requestBody)
        assertEquals("{}", request.responseBody)
    }

    @Test
    fun `other hosts pass through and are not recorded`() {
        val client = clientWith(FakeBackendInterceptor(recorder = { recorded += it }))

        val response = client.newCall(
            Request.Builder().url("https://${Environment.Production.url}/auth/user").build()
        ).execute()

        assertEquals("real", response.body.string())
        assertEquals(1, networkCallCount)
        assertTrue(recorded.isEmpty())
    }

    @Test
    fun `a failing recorder still gets the canned response`() {
        val client = clientWith(FakeBackendInterceptor(recorder = { error("disk full") }))

        val response = client.newCall(
            Request.Builder().url("https://${Environment.Fake.url}/auth/user").build()
        ).execute()

        assertEquals(200, response.code)
        assertTrue(response.body.string().contains(FakeBackendResponses.USER_ID))
    }

    @Test
    fun `device logs list is empty`() {
        val client = clientWith(FakeBackendInterceptor(recorder = { recorded += it }))

        val response = client.newCall(
            Request.Builder().url("https://${Environment.Fake.url}/v1/users/fake-user/device_logs").build()
        ).execute()

        assertEquals(200, response.code)
        assertEquals("[]", response.body.string())
    }

    @Test
    fun `device logs with a matching digest are created`() {
        val client = clientWith(FakeBackendInterceptor(recorder = { recorded += it }, now = { fixedNow }))

        val response = client.newCall(deviceLogsRequest(digest = "MD5=$DEVICE_LOGS_MD5")).execute()

        assertEquals(201, response.code)
        assertEquals(
            """{"id":"fake-device-logs","userId":"fake-user","digestMD5":"$DEVICE_LOGS_MD5",""" +
                """"mediaType":"application/json","size":17,"createdTime":"2026-09-29T10:00:00Z",""" +
                """"startAtTime":"2026-09-29T08:00:00.000Z","endAtTime":"2026-09-29T09:00:00.000Z"}""",
            response.body.string(),
        )
        assertEquals(DEVICE_LOGS_BODY, recorded.single().requestBody)
    }

    @Test
    fun `device logs with a digest that does not match the body are rejected`() {
        val client = clientWith(FakeBackendInterceptor(recorder = { recorded += it }))

        val response = client.newCall(deviceLogsRequest(digest = "MD5=AAAAAAAAAAAAAAAAAAAAAA==")).execute()

        assertEquals(400, response.code)
        assertTrue(response.body.string().contains("digests-not-equal"))
        assertEquals(400, recorded.single().status)
    }

    private fun deviceLogsRequest(digest: String) = Request.Builder()
        .url("https://${Environment.Fake.url}/v1/users/fake-user/device_logs")
        .header("Digest", digest)
        .header("X-Logs-Start-At-Time", "2026-09-29T08:00:00.000Z")
        .header("X-Logs-End-At-Time", "2026-09-29T09:00:00.000Z")
        .post(DEVICE_LOGS_BODY.toRequestBody("application/json".toMediaType()))
        .build()

    private fun clientWith(interceptor: FakeBackendInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(network)
        .build()

    private companion object {
        const val DEVICE_LOGS_BODY = """[{"type":"send"}]"""

        // printf '[{"type":"send"}]' | openssl md5 -binary | base64
        const val DEVICE_LOGS_MD5 = "oR0/4YWID8W/FiqpMzEBng=="
    }
}
