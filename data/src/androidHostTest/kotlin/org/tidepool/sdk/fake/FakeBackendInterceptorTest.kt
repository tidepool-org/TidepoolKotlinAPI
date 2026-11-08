package org.tidepool.sdk.fake

import kotlinx.datetime.Instant
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

    private fun clientWith(interceptor: FakeBackendInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(network)
        .build()
}
