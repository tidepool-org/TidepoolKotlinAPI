package org.tidepool.sdk

data class SrvRecord(
    val priority: Int,
    val weight: Int,
    val port: Int,
    val target: String
)


