package org.tidepool.sdk

data class Environment(val url: String) {
    companion object {
        val Producion = Environment("app.tidepool.org")
    }
}
