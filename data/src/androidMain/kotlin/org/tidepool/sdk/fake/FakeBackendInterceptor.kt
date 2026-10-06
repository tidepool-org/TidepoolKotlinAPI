package org.tidepool.sdk.fake

import co.touchlab.kermit.Logger
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.tidepool.sdk.Environment

/**
 * Answers every request to [Environment.Fake] in-process with a canned response from
 * [FakeBackendResponses] and hands it to [recorder]. Requests to any other host pass through
 * untouched, so the real backend keeps working in the same client.
 */
internal class FakeBackendInterceptor(
    private val recorder: FakeBackendRecorder,
    private val now: () -> Instant = { Clock.System.now() },
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.host != Environment.Fake.url) {
            return chain.proceed(request)
        }

        val fakeResponse = FakeBackendResponses.respond(
            method = request.method,
            pathSegments = request.url.pathSegments,
        )
        record(request, fakeResponse)

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(fakeResponse.status)
            .message(fakeResponse.reason)
            .header("Content-Type", JSON_CONTENT_TYPE)
            .body(fakeResponse.body.toResponseBody(JSON_CONTENT_TYPE.toMediaType()))
            .build()
    }

    // A throw here would escape on OkHttp's dispatcher thread and crash the app, so a failing
    // recorder only costs the log entry.
    private fun record(request: Request, response: FakeResponse) = runCatching {
        recorder.record(
            RecordedRequest(
                time = now(),
                method = request.method,
                url = request.url.toString(),
                status = response.status,
                requestBody = request.body?.let { body -> Buffer().also(body::writeTo).readUtf8() },
                responseBody = response.body,
            )
        )
    }.onFailure {
        Logger.e(TAG, it) { "Recording a fake backend request failed" }
    }

    private companion object {
        const val TAG = "FakeBackendInterceptor"
        const val JSON_CONTENT_TYPE = "application/json"
    }
}
