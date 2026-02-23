package org.tidepool.sdk

data class Environment(val url: String) {
    companion object {
        val Production = Environment("app.tidepool.org")
    }
}
