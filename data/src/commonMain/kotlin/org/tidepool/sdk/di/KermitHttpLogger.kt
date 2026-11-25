package org.tidepool.sdk.di

import io.ktor.client.plugins.logging.Logger

/**
 * Sends the HTTP client's log lines to Kermit, at info as Ktor's own Android logger does, so the host app decides
 * where they go. Path segments that are IDs, such as a user or data set ID, are replaced with `***`.
 */
internal class KermitHttpLogger : Logger {
    override fun log(message: String) {
        co.touchlab.kermit.Logger.i(TAG) { redactIds(message) }
    }

    internal companion object {
        const val TAG = "Ktor Client"

        private val idSegment = Regex("""(?<=/)[0-9a-fA-F]{10,}(?=[/?#\s]|$)""", RegexOption.MULTILINE)

        fun redactIds(message: String): String = idSegment.replace(message, "***")
    }
}
