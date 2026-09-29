package org.tidepool.sdk.fake

import kotlinx.datetime.Instant

/** One request answered by the fake backend, with the canned response it got. */
data class RecordedRequest(
    val time: Instant,
    val method: String,
    val url: String,
    val status: Int,
    val requestBody: String?,
    val responseBody: String,
)

/**
 * Receives every request sent to [org.tidepool.sdk.Environment.Fake]. Passing one to the SDK is
 * what turns the fake backend on. Called on network threads, so implementations must be
 * thread-safe and shouldn't block for long.
 */
fun interface FakeBackendRecorder {
    fun record(request: RecordedRequest)
}
