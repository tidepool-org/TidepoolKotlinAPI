package org.tidepool.sdk.model.summary

data class GlucoseRange(
    val glucose: Double,
    val minutes: Int,
    val records: Int,
    val percent: Double,
    val variance: Double
)