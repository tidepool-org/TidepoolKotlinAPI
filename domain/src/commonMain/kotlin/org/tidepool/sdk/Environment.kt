package org.tidepool.sdk

data class Environment(val url: String) {
    companion object {
        val Production = Environment("app.tidepool.org")

        /**
         * A developer-only backend answered in-process when a
         * [org.tidepool.sdk.fake.FakeBackendRecorder] is passed to the SDK. `.invalid` never resolves (RFC 2606), so without one, requests fail
         * locally instead of reaching any real host.
         */
        val Fake = Environment("fake.tidepool.invalid")
    }
}
