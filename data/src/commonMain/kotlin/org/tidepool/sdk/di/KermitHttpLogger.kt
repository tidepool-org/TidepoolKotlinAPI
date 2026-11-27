package org.tidepool.sdk.di

import io.ktor.client.plugins.logging.Logger

/**
 * Sends the HTTP client's log lines to Kermit, at info with Ktor's tag as Ktor's own Android logger does, so the host
 * app decides where they go. The client blanks identifiers before any logger gets a line.
 */
internal class KermitHttpLogger : Logger {
    override fun log(message: String) {
        co.touchlab.kermit.Logger.i(TAG) { message }
    }

    internal companion object {
        const val TAG = "Ktor Client"
    }
}
